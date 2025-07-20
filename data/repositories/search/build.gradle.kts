plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(projects.domain.vod)
    implementation(projects.envConfig)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.androidx.core.ktx)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit.jupiter.api)
    implementation(libs.kotlinx.datetime)

}
tasks.withType<Test> {
    useJUnitPlatform()
}