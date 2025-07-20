import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.android.library")
        project.pluginManager.apply("org.jetbrains.kotlin.android")

        project.configureAndroidLibrary()

        val libs = project.rootProject.extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        project.dependencies.apply {
            add("implementation", libs.findLibrary("androidx.core.ktx").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("junit.jupiter").get())
            add("testRuntimeOnly", libs.findLibrary("junit.jupiter.engine").get())
            add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
            add("testImplementation", libs.findLibrary("mockk").get())
            add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            add("testImplementation", libs.findLibrary("truth").get())
            add("testImplementation", project.dependencies.kotlin("test"))
        }

        project.tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}

