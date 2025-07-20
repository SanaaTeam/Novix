plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.core.ktx)
    implementation(projects.domain.vod)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(libs.junit.jupiter.api)

}

tasks.withType<Test> {
    useJUnitPlatform()
}