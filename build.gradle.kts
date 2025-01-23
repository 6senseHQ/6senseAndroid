// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false

}

//dokka {
//    moduleName.set(project.name)
//    moduleVersion.set(project.version.toString())
////    dokkaSourceSets.create("app") {
////        displayName.set("app")
////        sourceRoots.from(file("src/main/java"))
////        includes.from("buildSrc/src/main/java/ProjectConfig.kt") // Include additional files
////    }
//
//    dokkaSourceSets {
//        dokkaSourceSets.configureEach {
//            documentedVisibilities(
//                VisibilityModifier.Public,
//                VisibilityModifier.Protected,
//                VisibilityModifier.Package,
//                VisibilityModifier.Private
//            )
//            suppressGeneratedFiles = true
//            jdkVersion.set(21)
////
//            perPackageOption {
//                matchingRegex.set(".*/build/generated/.*")
//                suppress.set(true)
//                reportUndocumented.set(false)
//            }
//            sourceRoots.from(file("src/main/java"))
//            includes.from("buildSrc/src/main/java/ProjectConfig.kt") // Include additional files
////            includes.from("README.md") // Include additional files
//
//            sourceLink {
//                localDirectory.set(file("src/main/java"))
//                remoteUrl("https://github.com/6sense-Bangladesh/ome-android/tree/ome-dev/app/src/main")
//                remoteLineSuffix.set("#L")
//            }
//        }
//
//
//    }
//    pluginsConfiguration.html {
////        customAssets.from("logo.png")
//        footerMessage.set("(c) 6sense Technology")
//    }
//    dokkaPublications.html {
//        outputDirectory.set(file("documentation"))
//    }
//}
