import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {

        val libs =
            rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

        pluginManager.apply {
            apply(libs.findPlugin("android.application").get().get().pluginId)
            apply(libs.findPlugin("kotlin.android").get().get().pluginId)
            apply(libs.findPlugin("kotlin.compose").get().get().pluginId)
        }

        val javaVersion =
            JavaVersion.toVersion(libs.findVersion("javaVersion").get().toString())

        extensions.configure<ApplicationExtension> {
            compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

            defaultConfig {
                applicationId = libs.findVersion("applicationId").get().toString()
                minSdk = libs.findVersion("minSdk").get().toString().toInt()
                targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                testInstrumentationRunner = libs.findVersion("testRunner").get().toString()

                val ciCode = System.getenv("CI_VERSION_CODE")?.toIntOrNull()
                val ciName = System.getenv("CI_VERSION_NAME")

                versionCode = ciCode ?: libs.findVersion("versionCode").get().toString().toInt()
                versionName = ciName ?: libs.findVersion("versionName").get().toString()

                ndk {
                    abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
                }
            }

            buildTypes {
                getByName("release").apply {
                    isMinifyEnabled = true
                    isShrinkResources = true
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

            buildFeatures.apply {
                compose = true
                buildConfig = true
                viewBinding = false
                dataBinding = false
                mlModelBinding = true
                aidl = false
                prefab = false
                renderScript = false
                shaders = false
            }

            (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                jvmTarget = javaVersion.toString()
            }
        }
    }
}


