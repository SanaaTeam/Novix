plugins {
    alias(libs.plugins.novix.kotlin)
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
