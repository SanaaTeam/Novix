
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
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit.jupiter)
    ksp(libs.androidx.room.compiler)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
