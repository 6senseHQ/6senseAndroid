import org.gradle.api.JavaVersion

/**
 * Configuration object for the project.
 *
 * This object centralizes project-wide settings, such as package name, SDK versions,
 * app versions, and other constants. It promotes consistency and maintainability
 * by providing a single source of truth for these values.
 */
@Suppress("ConstPropertyName", "MemberVisibilityCanBePrivate")
object ProjectConfig {
    /**
     * The application's package name.
     */
    const val packageName = "com.six.sense"
    /**
     * The minimum SDK version supported by the application.
     */
    const val minSdk = 24
    /**
     * The SDK version used to compile the application.
     */
    const val compileSdk = 35
    /**
     * The target SDK version for the application.
     */
    const val targetSdk = 35

    /**
     * The version code of the application.
     */
    const val versionCode = 1
    /**
     * The version name of the application.
     */
    const val versionName = "1.0.0-dev"

    /**
     * The Java version used by the project.
     */
    val javaVersion = JavaVersion.VERSION_23

    /**
     * Flag indicating whether internal testing is enabled.
     */
    var IS_INTERNAL_TESTING = true

    /**
     * The base URL for API requests.
     */
    const val BASE_URL = "yk.com"
}