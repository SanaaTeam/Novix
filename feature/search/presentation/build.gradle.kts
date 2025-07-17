plugins {
    alias(libs.plugins.kotlin.serialization)
    id("plugins.android.Compose.library.convention")
}

android{
    namespace= "com.sanaa.presentation"
}

dependencies {
    implementation(project(":design_system"))
    implementation(project(":domain:vod"))
    implementation(project(":image_viewer"))
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(libs.androidx.ui.compose.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.compose.tooling.preview)
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