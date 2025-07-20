plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(projects.domain.vod)
    implementation(projects.preferences)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlinx.datetime)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit.jupiter.api)

}