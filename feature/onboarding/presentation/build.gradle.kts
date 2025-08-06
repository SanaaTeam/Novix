plugins {
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.presentation"
}

dependencies {
    implementation(projects.designSystem)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.bundles.compose)

    implementation(libs.androidx.compose.material3.material3)

    implementation(libs.androidx.ui.compose.foundation)

    implementation(projects.feature.authentication.api)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}