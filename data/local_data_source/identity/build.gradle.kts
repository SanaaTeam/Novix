plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
}
android {
    namespace = "com.sanaa.data.localdatasource.identity"
}

dependencies {
    implementation(project(":data:repositories:identity"))
    implementation(projects.domain.vod)
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
}