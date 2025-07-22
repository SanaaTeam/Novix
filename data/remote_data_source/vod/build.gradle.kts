import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)

}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("keys.properties")))

android {
    namespace = "com.sanaa.data.remotedatasource.vod"

    defaultConfig {
        val apiKey = localProperties["TMDB_API_KEY"].toString()
        buildConfigField("String", "TMDB_API_KEY", "\"${apiKey.trim()}\"")
        buildConfigField("String", "TMDB_URL", "\"https://api.themoviedb.org/3\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.preferences)
    implementation(projects.data.repositories.vod)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)
    implementation(libs.ktor.client.mock)
}