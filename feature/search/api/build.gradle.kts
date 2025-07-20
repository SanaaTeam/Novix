plugins {
    alias(libs.plugins.novix.android.compose)
}

dependencies {

    implementation(projects.feature.mediaDetails.api)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
}