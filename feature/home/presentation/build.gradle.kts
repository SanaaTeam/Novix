plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.presentation"
}

dependencies {
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.domain.vod)
    implementation(projects.designSystem)
    implementation(projects.imageViewer)

    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
}