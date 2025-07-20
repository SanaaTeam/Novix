plugins {
    alias(libs.plugins.novix.android.library)
}

dependencies {
    // Koin DI
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}