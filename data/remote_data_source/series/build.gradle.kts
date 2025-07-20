import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("keys.properties")))

android {
    namespace = "com.sanaa.series"

    defaultConfig {
        val apiKey = localProperties["TMDB_API_KEY"].toString()
        buildConfigField("String", "TMDB_API_KEY", "\"${apiKey.trim()}\"")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.envConfig)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)
    implementation(projects.data.repositories.series)
    implementation(libs.androidx.core.ktx)

}

tasks.withType<Test> {
    useJUnitPlatform()
}
