plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
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
            isMinifyEnabled = true
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
    implementation(projects.data.repositories.search)
    implementation(projects.preferences)
    implementation(projects.domain.vod)
    implementation(libs.androidx.core.ktx)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit.jupiter)
    ksp(libs.androidx.room.compiler)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
