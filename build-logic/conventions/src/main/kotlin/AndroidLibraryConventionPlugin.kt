import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.rootProject
            .extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        val testRunner = libs.findVersion("testRunner").get().toString()
        val javaVersion = JavaVersion.toVersion(libs.findVersion("javaVersion").get().toString())
        val composeCompilerVersion = libs.findVersion("composeBomVersion").get().toString()

        project.pluginManager.apply("com.android.library")
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        project.extensions.configure<LibraryExtension> {
            compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
            namespace = project.name.replace("-", ".")

            defaultConfig {
                minSdk = libs.findVersion("minSdk").get().toString().toInt()
                testInstrumentationRunner = testRunner
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
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

            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = composeCompilerVersion
            }

            (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                jvmTarget = javaVersion.toString()
            }
        }
    }
}
