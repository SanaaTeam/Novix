plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidComposeLibraryConvention") {
            id = libs.plugins.novix.android.compose.get().pluginId
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }
        register("androidLibraryConvention") {
            id = libs.plugins.novix.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
