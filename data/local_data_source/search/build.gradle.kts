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
    implementation(projects.data.repositories.search)
    implementation(projects.preferences)
    implementation(projects.domain.vod)

    implementation(libs.androidx.core.ktx)

    // Room dependencies
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // Testing
    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.test.runtime)

    implementation(libs.bundles.room)
    testImplementation(libs.bundles.room.testing)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
