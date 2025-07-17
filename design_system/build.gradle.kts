plugins {
    id("plugins.android.Compose.library.convention")
}

android {
    namespace = "com.sanaa.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(libs.androidx.ui.compose.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)

    debugImplementation(libs.androidx.ui.compose.tooling)
}