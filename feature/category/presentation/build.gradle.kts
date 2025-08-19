plugins {
    alias(libs.plugins.novix.android.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}


android {
    namespace = "com.sanaa.feature.category.presentation"
    packaging {
        resources {
            excludes += "feature/category/presentation/src/main/genre-images/**"
        }
    }
}


dependencies {
    implementation(projects.domain.identity)
    implementation(projects.domain.vod)
    implementation(projects.designSystem)
    implementation(projects.feature.category.api)
    implementation(projects.feature.mediaDetails.api)

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

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)

    testImplementation(libs.turbine)
    implementation(libs.kotlinx.datetime)


    implementation(libs.timber)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.slf4j.api)
}