plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(projects.domain.vod)

    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
}