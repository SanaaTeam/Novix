import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {

        val libs = getLibs()

        pluginManager.apply(libs.findPlugin("jetbrains.kotlin.jvm").get().get().pluginId)

        val javaVersion = JavaVersion.toVersion(libs.findVersion("javaVersion").get().toString())

        extensions.getByType(JavaPluginExtension::class.java).apply {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        extensions.getByType(KotlinJvmProjectExtension::class.java).compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        dependencies.apply {
            add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("junit.jupiter").get())
            add("testImplementation", libs.findLibrary("junit.jupiter.api").get())
            add("testRuntimeOnly", libs.findLibrary("junit.jupiter.engine").get())
            add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            add("testImplementation", libs.findLibrary("mockk").get())
            add("testImplementation", libs.findLibrary("truth").get())
            add("testImplementation", project.dependencies.kotlin("test"))
        }

        tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
        }
    }
}