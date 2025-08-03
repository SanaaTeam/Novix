plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sanaa.feature.playlists.presentation"
}

dependencies {
    implementation(projects.feature.playlists.api)
    implementation(projects.designSystem)
    implementation(projects.imageViewer)

    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))

    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)

    testImplementation(libs.turbine)
    implementation(libs.kotlinx.datetime)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}