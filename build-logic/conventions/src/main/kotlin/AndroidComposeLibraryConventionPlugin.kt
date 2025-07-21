import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType


class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {

        val libs =
            rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

        pluginManager.apply{
            apply(libs.findPlugin("android.library").get().get().pluginId)
            apply(libs.findPlugin("kotlin.android").get().get().pluginId)
            apply(libs.findPlugin("kotlin.compose").get().get().pluginId)
        }

        configureAndroidLibrary(
            testRunner = "androidx.test.runner.AndroidJUnitRunner"
        )

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}

