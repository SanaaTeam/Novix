plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.hilt.android)
}
android {
    namespace = "com.sanaa.identity"
}

dependencies {
    implementation(projects.data.repositories.identity)
    implementation(projects.domain.identity)
    implementation(projects.preferences)

    // Room dependencies
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.room.testing)

    implementation(libs.retrofit)
    implementation(libs.androidx.datastore.preferences)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.slf4j.api)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}