import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.novix.android.application)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val keysFile = rootProject.file("keys.properties")
val defaultFile = rootProject.file("default.properties")
when {
    keysFile.exists() -> localProperties.load(FileInputStream(keysFile))
    defaultFile.exists() -> localProperties.load(FileInputStream(defaultFile))
    else -> throw GradleException("No keys.properties or default.properties found!")
}

android {
    namespace = "com.sanaa.tvapp"

    lint {
        checkReleaseBuilds = false
        abortOnError = false
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
    }

    defaultConfig {
        val apiKey = localProperties["TMDB_API_KEY"].toString()
        buildConfigField("String", "TMDB_API_KEY", "\"${apiKey.trim()}\"")
        buildConfigField("String", "TMDB_URL", "\"https://api.themoviedb.org/3/\"")
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

    implementation(projects.preferences)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.datetime)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor)

    implementation(libs.androidx.datastore)
    implementation(libs.bundles.room)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    debugImplementation(libs.androidx.ui.compose.tooling)
    debugImplementation(libs.androidx.ui.compose.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.compose.test.junit4)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.perf)

    implementation(libs.tensorflow.lite.task.vision)

    implementation(libs.timber)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.runtime)
    testImplementation(libs.turbine)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.kotlinx.datetime)
}