import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sanaa.data.remotedatasource.vod"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.data.repositories.vod)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.ktor)
    implementation(libs.ktor.client.mock)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}