plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.feature.authentication.presentation"
}

dependencies {
    implementation(projects.domain.identity)
    implementation(projects.feature.home.api)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.navigation.testing.android)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

    implementation(projects.feature.mediaDetails.api)
    implementation(projects.designSystem)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material3)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    testImplementation(libs.bundles.test.runtime)

    testImplementation(libs.turbine)

    // Design System
    implementation(projects.designSystem)

    // Authentication API
    implementation(projects.feature.authentication.api)

    // Navigation
    implementation(projects.feature.mediaDetails.api)
}