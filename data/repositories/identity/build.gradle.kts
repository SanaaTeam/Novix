plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

android {
    namespace = "com.sanaa.data.repositories.vod"
}

dependencies {
    implementation(projects.domain.identity)
    implementation(projects.preferences)

    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging.common.android)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.junit)

    implementation(libs.slf4j.api)
}

