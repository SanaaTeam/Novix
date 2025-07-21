plugins {
    alias(libs.plugins.novix.android.compose)
}

kover {
    disable()
}

android {
    namespace = "com.sanaa.feature.mediadetails.api"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
}