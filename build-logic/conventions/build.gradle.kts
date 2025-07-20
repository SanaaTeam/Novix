plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibraryConvention") {
            id = libs.plugins.novix.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidComposeLibraryConvention") {
            id = libs.plugins.novix.android.compose.get().pluginId
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }

        register("kotlinLibraryConvention") {
            id = libs.plugins.novix.kotlin.get().pluginId
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}