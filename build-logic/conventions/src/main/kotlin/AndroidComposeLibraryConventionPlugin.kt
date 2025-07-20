import org.gradle.api.Plugin
import org.gradle.api.Project


class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.android.library")
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        project.configureAndroidLibrary(
            testRunner = "androidx.test.runner.AndroidJUnitRunner"
        )
    }
}

