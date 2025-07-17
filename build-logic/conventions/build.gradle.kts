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
            id = "plugins.android.library.convention"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
