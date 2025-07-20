plugins {
    alias(libs.plugins.novix.kotlin)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.test.runtime)
}