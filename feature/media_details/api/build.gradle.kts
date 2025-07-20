plugins {
    alias(libs.plugins.novix.android.compose)
}

kover {
    disable()
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
}