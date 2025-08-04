plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}
val excludedPackages = listOf(
    "*.R",
    "*.R_*",
    "**.di.**",
    "**.dao.**",
    "**.dto.**",
    "**.db.**",
    "**.util.**",
    "**.response.**",
    "**.entity.**",
    "**.exceptions.**",
    "**.logging.**",
    "*.BuildConfig*",
    "*.Manifest*",
    "com.sanaa.novix.ui.theme.*",
    "*.ComposableSingletons*",
    "*.MainActivity*",
    "*.NovixApp*",
    "com.sanaa.image_viewer.*",
    "com.sanaa.designsystem.*",
    "com.sanaa.vod.dataSource.**",
    "com.sanaa.movies.MovieApiService.*",

    "search.usecase.search_param.**",
    "com.sanaa.vod.mapper.*",
    "com.sanaa.presentation.screen.componants.*",
    "com.sanaa.presentation.component.**",
    "com.sanaa.presentation.shared_component.*",
    "com.sanaa.presentation.model.*",
    "com.sanaa.presentation.details_base.*",
    "com.sanaa.presentation.screen.state.*",
    "com.sanaa.presentation.screen.SearchScreen*",
    "com.sanaa.presentation.navigation.**",
    "com.sanaa.presentation.screen.actor.componants.**",
    "com.sanaa.presentation.screen.actor.screen.**",
    "com.sanaa.presentation.screen.episodeDetails.components.**",
    "com.sanaa.presentation.screen.episodeDetails.screen.**",
    "com.sanaa.presentation.screen.episodeDetails.**",
    "com.sanaa.presentation.screen.movieDetails.components.**",
    "com.sanaa.presentation.screen.genreMovies.**",
    "com.sanaa.presentation.screen.genreTvShows.GenreTvShowsScreen*",
    "com.sanaa.presentation.screen.movieDetails.**",
    "com.sanaa.presentation.screen.review.components.**",
    "com.sanaa.presentation.screen.review.ReviewsScreen*",
    "com.sanaa.presentation.screen.series.components.**",
    "com.sanaa.presentation.screen.series.SeriesScreen*",
    "feature.media_details.api**",
    "com.sanaa.presentation.cards.**",
    "com.sanaa.presentation.components.**",
    "com.sanaa.presentation.app.**",
    "com.sanaa.presentation.api.**",
    "com.sanaa.presentation.**",
    "com.sanaa.presentation.screen.**",
    "com.sanaa.presentation.util.**",
    "com.sanaa.presentation.state.**",
    "com.sanaa.vod.media.**",
    "com.sanaa.presentation.modifier.**",
    "dagger.hilt.**",
    "hilt_aggregated_deps.**",
    "**_Factory",
    "**_HiltModules*",
    "Hilt_*",
    "com.sanaa.novix.Hilt_*",
    "com.sanaa.novix.*_Factory",
    "com.sanaa.novix.*_MembersInjector",
    "com.sanaa.vod.util.*",
    "com.sanaa.presentation.cards.**",

    "com.sanaa.presentation.screen.login.**",
    "com.sanaa.presentation.screen.myRating.**",
    "com.sanaa.presentation.screen.login.components.**",
    "com.sanaa.presentation.screen.login_base.**",
    "com.sanaa.presentation.screen.welcome.**",
    "com.sanaa.presentation.screen.welcome.components.**",
    "com.sanaa.presentation.webview.**",
    "com.sanaa.presentation.modifier.**",
    "com.sanaa.presentation.api.**",
    "com.sanaa.novix.resourceProvider.**",
    "com.sanaa.vod.network.interceptor.**",
    "com.sanaa.identity.dataSoruce.dataStore.**",
    "**.proto.**",
    "**.network.**",
    "com.sanaa.identity.dataSoruce.local.dataStore.**",
    "com.sanaa.vod.dataSource.remote.search.search.**",
    "com.sanaa.presentation.base.**",
    "**.util.**",
    "**.mapper.**",
    "**.api.**",
)

val profileScreenPackages = listOf(
    "com.sanaa.presentation.screen.MyAccountScreen*",
    "com.sanaa.presentation.screen.myAccount.component.**",
)

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    kover {
        reports {
            filters {
                excludes {
                    classes(
                        excludedPackages +
                                profileScreenPackages
                    )
                }
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
    kover(projects.preferences)
    kover(projects.app)
    kover(projects.data.repositories.vod)
    kover(projects.data.remoteDataSource.vod)
    kover(projects.data.localDataSource.vod)
    kover(projects.domain.vod)
    kover(projects.feature.authentication.api)
    kover(projects.feature.authentication.presentation)
    kover(projects.feature.home.api)
    kover(projects.feature.home.presentation)
    kover(projects.feature.userProfile.api)
    kover(projects.feature.userProfile.presentation)
    kover(projects.feature.playlists.api)
    kover(projects.feature.playlists.presentation)
    kover(projects.feature.search.api)
    kover(projects.feature.search.presentation)
    kover(projects.feature.mediaDetails.api)
    kover(projects.feature.mediaDetails.presentation)
}