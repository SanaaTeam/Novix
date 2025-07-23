plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
}
android {
    namespace = "com.sanaa.data.localdatasource.vod"
}

dependencies {


    implementation(projects.data.repositories.vod)
    implementation(projects.preferences)
    implementation(projects.domain.vod)

    // Room dependencies
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.room.testing)


    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}