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
            id = "plugins.android.Compose.library.convention"
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }
    }
}
