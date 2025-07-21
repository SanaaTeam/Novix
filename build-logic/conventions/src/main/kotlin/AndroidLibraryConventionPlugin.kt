import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {

        val libs = project.rootProject.extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        pluginManager.apply {
            apply(libs.findPlugin("android.library").get().get().pluginId)
            apply(libs.findPlugin("kotlin.android").get().get().pluginId)
        }

        configureAndroidLibrary()

        dependencies.apply {
            add("implementation", libs.findLibrary("androidx.core.ktx").get())
            add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("junit.jupiter").get())
            add("testImplementation", libs.findLibrary("junit.jupiter.api").get())
            add("testRuntimeOnly", libs.findLibrary("junit.jupiter.engine").get())
            add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
            add("testImplementation", libs.findLibrary("mockk").get())
            add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            add("testImplementation", libs.findLibrary("truth").get())
            add("testImplementation", dependencies.kotlin("test"))
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}

