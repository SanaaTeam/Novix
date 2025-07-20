plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
}

dependencies {
    implementation(projects.domain.vod)
    implementation(projects.preferences)

    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging.common.android)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.slf4j.api)
}

