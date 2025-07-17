plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kotlinLibraryConvention") {
            id = libs.plugins.novix.kotlin.get().pluginId
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}