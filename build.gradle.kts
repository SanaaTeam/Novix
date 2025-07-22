// Top-level build file where you can add configuration options common to all sub-projects/modules.
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
    "*.NovixApp*",
    "com.sanaa.image_viewer.*",
    "com.sanaa.designsystem.*",
    "com.sanaa.search.dto.*",
    "com.sanaa.search.di.*",
    "com.sanaa.movies.MovieApiService.*",

    "entity.**",
    "search.usecase.search_param.**",
    "**.dao.**",
    "**.dto.**",
    "**.db.**",
    "**.response.**",
    "exceptions.**",

    "com.sanaa.vod.mapper.*",
    "com.sanaa.presentation.filter_bottomsheet.components.**",
    "com.sanaa.presentation.screen.componants.*",
    "com.sanaa.presentation.component.**",
    "com.sanaa.presentation.model.*",
    "com.sanaa.presentation.details_base.*",
    "com.sanaa.presentation.screen.state.*",
    "com.sanaa.presentation.screen.SearchScreen*",
    "com.sanaa.presentation.filter_bottomsheet.FilterBottomSheet*",
    "com.sanaa.search.search_result.db.*",
    "com.sanaa.presentation.navigation.**",
    "com.sanaa.presentation.screen.actor.componants.**",
    "com.sanaa.presentation.screen.actor.screen.**",
    "com.sanaa.presentation.screen.episode_details.components.**",
    "com.sanaa.presentation.screen.episode_details.**",
    "com.sanaa.presentation.screen.movie_details.components.**",
    "com.sanaa.presentation.screen.movie_categories.**",
    "com.sanaa.presentation.screen.movie_details.**",
    "com.sanaa.presentation.screen.review.components.**",
    "com.sanaa.presentation.screen.review.ReviewsScreen*",
    "com.sanaa.presentation.screen.series.components.**",
    "com.sanaa.presentation.screen.series.SeriesScreen*",
    "feature.media_details.api**",
    "com.sanaa.presentation.cards.**"
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