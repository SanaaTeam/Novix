import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("keys.properties")))

android {
    namespace = "com.sanaa.identity"

    defaultConfig {
        val apiKey = localProperties["TMDB_API_KEY"].toString()
        buildConfigField("String", "TMDB_API_KEY", "\"${apiKey.trim()}\"")
        buildConfigField("String", "TMDB_URL", "\"https://api.themoviedb.org/\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.preferences)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.koin)
    implementation(libs.ktor.client.mock)
    implementation(libs.slf4j.api)
    implementation(libs.converter.gson)
}