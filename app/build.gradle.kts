plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = libs.versions.testRunner.get()

        val ciCode = System.getenv("CI_VERSION_CODE")?.toIntOrNull()
        val ciName = System.getenv("CI_VERSION_NAME")

        versionCode = ciCode ?: libs.versions.versionCode.get().toInt()
        versionName = ciName ?: libs.versions.versionName.get()
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            manifestPlaceholders["crashlytics_debug"] = "true"
            manifestPlaceholders["analytics_debug"] = "true"
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
        val javaVer = JavaVersion.toVersion(libs.versions.javaVersion.get())
        sourceCompatibility = javaVer
        targetCompatibility = javaVer
    }

    kotlinOptions {
        jvmTarget = libs.versions.javaVersion.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(projects.envConfig)
    implementation(projects.domain.vod)
    implementation(projects.feature.search.presentation)
    implementation(projects.data.repositories.search)
    implementation(projects.data.remoteDataSource.search)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(libs.androidx.ui.compose.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.compose.test.junit4)

    debugImplementation(libs.androidx.ui.compose.tooling)
    debugImplementation(libs.androidx.ui.compose.test.manifest)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)

    implementation(libs.timber)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)


    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.cloudy)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.tensorflow.lite.task.vision)

    implementation(libs.kotlinx.serialization.json)

    // Local Data Sources
    implementation(projects.data.localDataSource.search)

    // Language Provider
    implementation(projects.envConfig)

    implementation(projects.feature.search.api)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.savedContent.api)
    implementation(projects.feature.savedContent.presentation)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.onboarding.api)
    implementation(projects.feature.onboarding.presentation)
    implementation(projects.feature.userProfile.api)
    implementation(projects.feature.userProfile.presentation)
    implementation(projects.feature.authentication.api)
    implementation(projects.feature.authentication.presentation)
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.feature.mediaDetails.presentation)
    implementation(projects.feature.playlists.api)
    implementation(projects.feature.playlists.presentation)
}