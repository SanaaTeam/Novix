package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.presentation.screen.actor.screen.ActorGalleryScreen
import com.sanaa.presentation.screen.actor.screen.ActorScreen
import com.sanaa.presentation.screen.actor.screen.TopMoviesScreen
import com.sanaa.presentation.screen.actor.screen.TopSeriesScreen

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
        NavHost(
            navController = navController,
            startDestination = initialRoute,
        ) {

            /* ── actor details ── */
            composable(
                route = ActorDetailsScreenRoute.PATTERN,
                arguments = listOf(navArgument(ActorDetailsScreenRoute.ARG_ACTOR_ID) {
                    type = NavType.IntType
                })
            ) { entry ->
                val actorId = entry.arguments!!.getInt(ActorDetailsScreenRoute.ARG_ACTOR_ID)

                ActorScreen(actorId = actorId)
            }

            /* ── top movies ── */
            composable(
                route = TopMoviesScreenRoute.PATTERN,
                arguments = listOf(navArgument(TopMoviesScreenRoute.ARG_ACTOR_ID) {
                    type = NavType.IntType
                })
            ) { entry ->
                val actorId = entry.arguments!!.getInt(TopMoviesScreenRoute.ARG_ACTOR_ID)

                TopMoviesScreen(
                    actorId = actorId,
                    navigateBack = { navController.popBackStack() }
                )
            }

            /* ── top series ── */
            composable(
                route = TopSeriesScreenRoute.PATTERN,
                arguments = listOf(navArgument(TopSeriesScreenRoute.ARG_ACTOR_ID) {
                    type = NavType.IntType
                })
            ) { entry ->
                val actorId = entry.arguments!!.getInt(TopSeriesScreenRoute.ARG_ACTOR_ID)
                TopSeriesScreen(
                    actorId = actorId,
                    navigateBack = { navController.popBackStack() }
                )
            }

            /* ── gallery ── */
            composable(
                route = ActorGalleryScreenRoute.PATTERN,
                arguments = listOf(navArgument(ActorGalleryScreenRoute.ARG_ACTOR_ID) {
                    type = NavType.IntType
                })
            ) { entry ->
                val actorId = entry.arguments!!.getInt(ActorGalleryScreenRoute.ARG_ACTOR_ID)
                ActorGalleryScreen(
                    actorId = actorId,
                    navigateBack = { navController.popBackStack() }
                )
            }

        }

    }
}

enum class StartRoute {
    SERIES, MOVIE, ACTOR
}