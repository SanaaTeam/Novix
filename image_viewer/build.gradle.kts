plugins {
    alias(libs.plugins.novix.android.compose)
}
android {
    namespace = "com.sanaa.image_viewer"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.tensorflow.lite.task.vision)
}