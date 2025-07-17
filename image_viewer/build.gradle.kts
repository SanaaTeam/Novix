plugins {
    id("plugins.android.library.convention")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    api(libs.coil.compose)
    implementation(libs.cloudy)
    // Task Vision library for classifiers (includes JNI interpreter)
    implementation(libs.tensorflow.lite.task.vision)
}