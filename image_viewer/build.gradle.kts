plugins {
    alias(libs.plugins.novix.android.compose)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.coil.compose)
    implementation(libs.cloudy)
    // Task Vision library for classifiers (includes JNI interpreter)
    implementation(libs.tensorflow.lite.task.vision)
}