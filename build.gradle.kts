plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.kover)
}

val excludedPackages = listOf(
    "*.R",
    "*.R_*",
    "**.di.**",
    "**.logging.**",
    "*.BuildConfig*",
    "*.Manifest*",
    "com.sanaa.novix.ui.theme.*",
    "*.ComposableSingletons*",
    "*.MainActivity*",
    "com.sanaa.image_viewer.*"
)

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    kover {
        reports {
            filters {
                excludes { classes(excludedPackages) }
            }
            total {
                verify {
                    rule { bound { minValue = 80 } }
                }
            }
        }
    }
}

dependencies {
    kover(projects.app)
    kover(projects.domain.authentication.repository)
    kover(projects.domain.movies.repository)
    kover(projects.domain.actors.repository)
    kover(projects.domain.series)
    kover(projects.domain.savedContent.repository)
    kover(projects.domain.search.repository)
    kover(projects.domain.userProfile.repository)

    kover(projects.data.remoteDataSource.actors)
    kover(projects.data.remoteDataSource.authentication)
    kover(projects.data.remoteDataSource.movies)
    kover(projects.data.remoteDataSource.savedContent)
    kover(projects.data.remoteDataSource.search)
    kover(projects.data.remoteDataSource.series)
    kover(projects.data.remoteDataSource.userProfile)

    kover(projects.data.localDataSource.actors)
    kover(projects.data.localDataSource.authentication)
    kover(projects.data.localDataSource.movies)
    kover(projects.data.localDataSource.savedContent)
    kover(projects.data.localDataSource.search)
    kover(projects.data.localDataSource.series)
    kover(projects.data.localDataSource.userProfile)

    kover(projects.data.repositories.actors)
    kover(projects.data.repositories.authentication)
    kover(projects.data.repositories.movies)
    kover(projects.data.repositories.savedContent)
    kover(projects.data.repositories.search)
    kover(projects.data.repositories.series)
    kover(projects.data.repositories.userProfile)

    kover(projects.feature.authentication.api)
    kover(projects.feature.authentication.presentation)
    kover(projects.feature.home.api)
    kover(projects.feature.home.presentation)
    kover(projects.feature.userProfile.api)
    kover(projects.feature.userProfile.presentation)
    kover(projects.feature.savedContent.api)
    kover(projects.feature.savedContent.presentation)
    kover(projects.feature.playlists.api)
    kover(projects.feature.playlists.presentation)
    kover(projects.feature.search.api)
    kover(projects.feature.search.presentation)
    kover(projects.feature.mediaDetails.api)
    kover(projects.feature.mediaDetails.presentation)
    kover(projects.feature.onboarding.api)
    kover(projects.feature.onboarding.presentation)
}
