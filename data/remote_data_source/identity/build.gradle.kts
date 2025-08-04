import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

android {
    namespace = "com.sanaa.data.remotedatasource.identity"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.preferences)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.mock)
    implementation(libs.slf4j.api)
    implementation(libs.converter.gson)
}