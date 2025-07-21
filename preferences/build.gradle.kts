plugins {
    alias(libs.plugins.novix.android.library)
}

android {
    namespace = "com.sanaa.preferences"
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}