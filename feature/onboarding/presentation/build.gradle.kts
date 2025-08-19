plugins {
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.feature.onboarding.presentation"
}

dependencies {
    implementation(projects.designSystem)
    implementation(projects.feature.onboarding.api)
    implementation(projects.feature.authentication.api)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.compose.foundation)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.foundation.android)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)
    testImplementation(libs.turbine)

}