plugins {
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.designsystem"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.material3)

    debugImplementation(libs.androidx.ui.compose.tooling)
}