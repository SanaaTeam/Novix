plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.feature.home.presentation"
}

dependencies {
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.domain.vod)
    implementation(projects.designSystem)
    implementation(projects.imageViewer)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)

    testImplementation(libs.turbine)
}