plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sanaa.feature.home.presentation"
}

dependencies {
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.domain.identity)
    implementation(projects.domain.vod)
    implementation(projects.designSystem)
    implementation(projects.imageViewer)
    implementation(projects.feature.home.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.userProfile.api)
    implementation(projects.feature.playlists.api)
    implementation(projects.feature.authentication.api)

    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.junit)

    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)

    testImplementation(libs.turbine)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}