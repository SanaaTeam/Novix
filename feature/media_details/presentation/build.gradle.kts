plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.novix.android.compose)
}

dependencies {
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.domain.vod)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.imageViewer)
    implementation(projects.domain.vod)
    implementation(projects.designSystem)
    implementation(projects.imageViewer)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.compose.tooling.preview)
    implementation(libs.coil.compose)

    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(libs.androidx.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation(libs.turbine)
    implementation(libs.kotlinx.datetime)
}


tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}