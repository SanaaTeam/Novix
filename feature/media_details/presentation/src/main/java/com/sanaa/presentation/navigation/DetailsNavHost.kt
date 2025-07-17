package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.actor.screen.ActorGalleryScreen
import com.sanaa.presentation.screen.actor.screen.ActorScreen
import com.sanaa.presentation.screen.actor.screen.TopMoviesScreen
import com.sanaa.presentation.screen.actor.screen.TopSeriesScreen
import com.sanaa.presentation.screen.episode_details.EpisodeDetailsScreen
import com.sanaa.presentation.screen.review.ReviewsScreen
import com.sanaa.presentation.screen.series.SeriesScreen

@Composable
fun DetailsNavHost(startRoute: StartRoute, id: Int) {
    val initialRoute = when (startRoute) {
        StartRoute.SERIES -> SeriesDetailsScreenRoute(id).route()
        StartRoute.MOVIE -> MovieDetailsScreenRoute(id).route()
        StartRoute.ACTOR -> ActorDetailsScreenRoute(id).route()
    }
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavControllerProvider provides navController
    ) {
        NovixTheme(isDarkMode = isSystemInDarkTheme()) {
            NavHost(
                navController = navController,
                startDestination = initialRoute,
                modifier = Modifier.background(Theme.colors.surface)
            ) {

                // Series Routs
                composable(
                    route = SeriesDetailsScreenRoute.PATTERN,
                    arguments = listOf(navArgument(SeriesDetailsScreenRoute.ARG_SERIES_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val seriesId = entry.arguments!!.getInt(SeriesDetailsScreenRoute.ARG_SERIES_ID)
                    SeriesScreen(seriesId = seriesId)
                }

                composable(
                    route = EpisodeDetailsScreenRoute.PATTERN, arguments = listOf(
                        navArgument(EpisodeDetailsScreenRoute.ARG_SERIES_ID) {
                            type = NavType.IntType
                        },
                        navArgument(EpisodeDetailsScreenRoute.ARG_SEASON_NUMBER) {
                            type = NavType.IntType
                        },
                        navArgument(EpisodeDetailsScreenRoute.ARG_EPISODE_NUMBER) {
                            type = NavType.IntType
                        },
                    )
                ) { entry ->
                    val seriesId = entry.arguments!!.getInt(EpisodeDetailsScreenRoute.ARG_SERIES_ID)
                    val seasonNumber =
                        entry.arguments!!.getInt(EpisodeDetailsScreenRoute.ARG_SEASON_NUMBER)
                    val episodeNumber =
                        entry.arguments!!.getInt(EpisodeDetailsScreenRoute.ARG_EPISODE_NUMBER)
                    EpisodeDetailsScreen(
                        seriesId = seriesId,
                        seasonNumber = seasonNumber,
                        episodeNumber = episodeNumber
                    )
                }


                // Actor Routs
                composable(
                    route = ActorDetailsScreenRoute.PATTERN,
                    arguments = listOf(navArgument(ActorDetailsScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val actorId = entry.arguments!!.getInt(ActorDetailsScreenRoute.ARG_ACTOR_ID)
                    ActorScreen(actorId = actorId)
                }

                composable(
                    route = TopMoviesScreenRoute.PATTERN,
                    arguments = listOf(navArgument(TopMoviesScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val actorId = entry.arguments!!.getInt(TopMoviesScreenRoute.ARG_ACTOR_ID)
                    TopMoviesScreen(
                        actorId = actorId, navigateBack = { navController.popBackStack() })
                }

                composable(
                    route = TopSeriesScreenRoute.PATTERN,
                    arguments = listOf(navArgument(TopSeriesScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val actorId = entry.arguments!!.getInt(TopSeriesScreenRoute.ARG_ACTOR_ID)
                    TopSeriesScreen(
                        actorId = actorId, navigateBack = { navController.popBackStack() })
                }

                composable(
                    route = ActorGalleryScreenRoute.PATTERN,
                    arguments = listOf(navArgument(ActorGalleryScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val actorId = entry.arguments!!.getInt(ActorGalleryScreenRoute.ARG_ACTOR_ID)
                    ActorGalleryScreen(
                        actorId = actorId, navigateBack = { navController.popBackStack() })
                }


                // Review Routs
                composable(
                    route = ReviewsScreenRoute.PATTERN,
                    arguments = listOf(navArgument(ReviewsScreenRoute.ARG_SERIES_ID) {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val seriesId = entry.arguments!!.getInt(ReviewsScreenRoute.ARG_SERIES_ID)
                    ReviewsScreen(
                        seriesId = seriesId
                    )
                }
            }
        }
    }
}

enum class StartRoute {
    SERIES, MOVIE, ACTOR
}