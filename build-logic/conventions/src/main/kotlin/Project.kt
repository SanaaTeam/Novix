import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware

fun Project.configureAndroidLibrary(
    testRunner: String = "androidx.test.runner.AndroidJUnitRunner"
) {
    val libs = rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
    val javaVersion = JavaVersion.toVersion(libs.findVersion("javaVersion").get().toString())

    extensions.configure<LibraryExtension> {
        compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().toString().toInt()
            testOptions.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            testInstrumentationRunner = testRunner
            consumerProguardFiles("consumer-rules.pro")
        }

        buildTypes {
            getByName("debug").apply {
                isMinifyEnabled = false
                isShrinkResources = false
            }
            getByName("release").apply {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
            jvmTarget = javaVersion.toString()
        }
    }
}
