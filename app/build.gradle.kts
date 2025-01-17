import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
}

android {
    namespace = ProjectConfig.packageName
    compileSdk = ProjectConfig.compileSdk

    signingConfigs {
        create("release") {
            storeFile = file("debug.keystore")
            keyAlias = "androiddebugkey"
            storePassword = "android"
            keyPassword = "android"
        }
        getByName("debug") {
            initWith(signingConfigs.getByName("release"))
        }
    }

    defaultConfig {
        applicationId = ProjectConfig.packageName
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    applicationVariants.all {
        outputs.forEach {
            val output = it as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "${rootProject.name.replace(' ', '_')}_v"
            output.outputFileName += "$versionName-$name.apk"
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlin {
        jvmToolchain(ProjectConfig.javaVersion.toString().toInt())
        compilerOptions {
            freeCompilerArgs.addAll("-Xcontext-receivers", "-Xwhen-guards", "-Xnon-local-break-continue")
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST,DEPENDENCIES,LICENSE.md,LICENSE-notice.md}"
            excludes += "mozilla/public-suffix-list.txt"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.ui.viewbinding)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.bundles.androidx.debug.test)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //gsm auth
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.identity)
    implementation(libs.google.auth.library.oauth2.http)

    //fb auth
    implementation(libs.facebook.android.sdk)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //splashscreen
    implementation(libs.androidx.core.splashscreen)

    // ktor
    implementation(libs.bundles.ktor)

    //chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    //Coroutine Worker
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.multiprocess)

    //dimen sdp ssp
    implementation(libs.sdp.compose)

    implementation(libs.androidx.material.icons.extended)

    // Play In-App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    implementation(libs.openai.java)

    dokkaPlugin(libs.android.documentation.plugin)
//    implementation(libs.dokka.android.gradle.plugin)

}

dokka {
    moduleName.set(rootProject.name + " Documentation")
    moduleVersion.set(ProjectConfig.versionName)

    dokkaSourceSets.main {
        sourceRoots.from(file("app/src/main/java"))
        jdkVersion.set(ProjectConfig.javaVersion.toString().toInt())
        documentedVisibilities(
            VisibilityModifier.Public,
            VisibilityModifier.Protected,
            VisibilityModifier.Package
        )
        sourceLink {
            localDirectory.set(file("app/src/main/java"))
            remoteUrl("https://github.com/6sense-Bangladesh/ome-android/tree/ome-dev/app/src/main/java")
            remoteLineSuffix.set("#L")
        }
    }
    dokkaSourceSets.configureEach {
        if(name != "main")
            suppress.set(true)
        suppressGeneratedFiles = true
        println(name)
    }

    pluginsConfiguration.html {
//        customAssets.from("logo.png")
        footerMessage.set("(c) 2025 6sense Technology")
    }
    dokkaPublications.html {
        outputDirectory.set(file("../documentation"))
    }
}
