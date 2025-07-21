plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

android {
    namespace = "com.sanaa.data.repositories.series"
}

dependencies {
    implementation(projects.domain.vod)

    implementation(libs.timber)

    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.serialization.kotlinx.json)
}