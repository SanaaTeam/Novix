import org.gradle.api.Plugin
import org.gradle.api.Project


class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply{
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        configureAndroidLibrary(
            testRunner = "androidx.test.runner.AndroidJUnitRunner"
        )
    }
}

