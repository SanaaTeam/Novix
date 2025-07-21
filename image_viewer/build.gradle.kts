plugins {
    alias(libs.plugins.novix.android.compose)
}
android {
    namespace = "com.sanaa.image_viewer"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.coil.compose)
    implementation(libs.cloudy)
    // Task Vision library for classifiers (includes JNI interpreter)
    implementation(libs.tensorflow.lite.task.vision)
}