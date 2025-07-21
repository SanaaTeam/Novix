plugins {
    alias(libs.plugins.novix.android.compose)
}

android {
    namespace = "com.sanaa.feature.search.api"
}

dependencies {
    implementation(projects.feature.mediaDetails.api)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
}