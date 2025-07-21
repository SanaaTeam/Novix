import org.gradle.api.Plugin
import org.gradle.api.Project


class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        configureAndroidLibrary(
            testRunner = "androidx.test.runner.AndroidJUnitRunner"
        )
    }
}

