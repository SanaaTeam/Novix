plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

android {
    namespace = "com.sanaa.data.repositories.actors"
}

dependencies {
    implementation(projects.domain.vod)
    implementation(projects.preferences)

    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.timber)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
    implementation(libs.kotlinx.datetime)
}