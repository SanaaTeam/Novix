plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.hilt.android)
}
android {
    namespace = "com.sanaa.data.localdatasource.vod"
}

dependencies {
    implementation(projects.data.repositories.vod)

    // Room dependencies
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.room.testing)


    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}