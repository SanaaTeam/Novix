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
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("keys.properties")))
android {
    namespace = libs.versions.namespace.get()

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

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(projects.data.repositories.vod)
    implementation(projects.data.remoteDataSource.vod)
    implementation(projects.data.localDataSource.vod)
    implementation(projects.feature.mediaDetails.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.authentication.api)
    implementation(projects.domain.vod)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.authentication.presentation)
    implementation(projects.feature.mediaDetails.presentation)
    implementation(projects.feature.authentication.presentation)
    implementation(projects.preferences)
    implementation(projects.domain.identity)
    implementation(projects.data.remoteDataSource.identity)
    implementation(projects.data.localDataSource.identity)
    implementation(projects.data.repositories.identity)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.material3)

    testImplementation(libs.bundles.test)
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
    implementation(libs.tensorflow.lite.task.vision)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.room)

}