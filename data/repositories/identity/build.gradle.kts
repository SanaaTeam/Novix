plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.sanaa.identity"
}

dependencies {
    implementation(projects.domain.identity)
    implementation(projects.data.remoteDataSource.identity)
    implementation(libs.bundles.retrofit)

    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging.common.android)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.junit)

    implementation(libs.slf4j.api)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}

