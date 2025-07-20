plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(projects.data.repositories.search)
    implementation(projects.envConfig)
    implementation(projects.domain.vod)
    implementation(libs.androidx.core.ktx)

    // Room dependencies
    implementation(libs.bundles.room)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.bundles.room.testing)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

}
tasks.withType<Test> {
    useJUnitPlatform()
}
