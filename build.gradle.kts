// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kover)
}
allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    kover {
        reports {
            filters {
                excludes {
                    classes(
                        "*.R",
                        "*.R_*",
                        "*.BuildConfig*",
                        "*.Manifest*",
                        "com.sanaa.novix.ui.theme.*",
                        "*.ComposableSingletons*",
                        "*.MainActivity*"
                    )
                }
            }
            total {
                verify {
                    rule {
                        bound {
                            minValue = 80
                        }
                    }
                }
            }
        }
    }
}
/*
dependencies {
    kover(projects.domain.authentication.repository)
    kover(projects.domain.movies.repository)
    kover(projects.domain.actors.repository)
    kover(projects.domain.series)
    kover(projects.domain.savedContent.repository)
    kover(projects.domain.search.repository)
    kover(projects.data.search.repository)
    kover(projects.data.savedContent.repository)
    kover(projects.data.userProfile.repository)
    kover(projects.data.remoteDataSource)
    kover(projects.data.localDataSource)
    kover(projects.data.actors.dataSource)
    kover(projects.data.actors.repository)
    kover(projects.data.authentication.dataSource.local)
    kover(projects.data.movies.dataSource.remote)
    kover(projects.data.savedContent.dataSource.remote)
    kover(projects.data.search.dataSource.remote)
    kover(projects.data.series.dataSource.remote)
    kover(projects.data.savedContent.dataSource.remote)
    kover(projects.data.actors.dataSource.local)
    kover(projects.data.authentication.dataSource.local)
    kover(projects.data.movies.dataSource.local)
    kover(projects.data.savedContent.dataSource.local)
    kover(projects.data.search.dataSource.local)
    kover(projects.data.series.dataSource.local)
    kover(projects.data.userProfile.dataSource.local)
    kover(projects.app)
    kover(projects.domain)
    kover(projects.feature.authentication.presentation)
    kover(projects.feature.authentication.api)
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
    kover(projects.data.authentication.repository)
    kover(projects.data.movies.repository)
    kover(projects.data.series.repository)
    kover(projects.data.search.repository)
}*/