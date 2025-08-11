plugins {
    alias(libs.plugins.novix.android.compose)
}


android {
    namespace = "com.sanaa.feature.category.api"
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))

}