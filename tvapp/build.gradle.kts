plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sanaa.tvapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sanaa.tvapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

    }

    buildTypes {
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.designSystem)
    implementation(projects.imageViewer)

    implementation(projects.domain.vod)
    implementation(projects.data.repositories.vod)
    implementation(projects.data.remoteDataSource.vod)
    implementation(projects.data.localDataSource.vod)

    implementation(projects.domain.identity)
    implementation(projects.data.repositories.identity)
    implementation(projects.data.remoteDataSource.identity)
    implementation(projects.data.localDataSource.identity)

    implementation(projects.feature.home.api)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.feature.mediaDetails.presentation)
    implementation(projects.feature.userProfile.api)
    implementation(projects.feature.userProfile.presentation)
    implementation(projects.feature.playlists.api)
    implementation(projects.feature.playlists.presentation)
    implementation(projects.feature.authentication.api)
    implementation(projects.feature.authentication.presentation)
    implementation(projects.preferences)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(libs.androidx.ui.compose.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.compose.test.junit4)
    debugImplementation(libs.androidx.ui.compose.tooling)
    debugImplementation(libs.androidx.ui.compose.test.manifest)

    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.tensorflow.lite.task.vision)

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
    implementation(libs.slf4j.api)
}