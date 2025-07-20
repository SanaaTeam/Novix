plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sanaa.search"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(projects.domain.vod)
    implementation(projects.preferences)

    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.androidx.core.ktx)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging.common.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)
    androidTestImplementation(libs.androidx.junit)

}
tasks.withType<Test> {
    useJUnitPlatform()
}