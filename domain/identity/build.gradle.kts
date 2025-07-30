plugins {
    alias(libs.plugins.novix.kotlin)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.kotlinx.datetime)

    // Dagger 2
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)
}
