package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.presentation.screens.actors.screen.ActorScreen
import com.sanaa.presentation.screens.actors.screen.TopMoviesScreen

@Composable
fun DetailsNavHost(startRoute: StartRoute, id: Int) {
    val initialRoute = when (startRoute) {
        StartRoute.SERIES -> SeriesDetailsScreenRoute(id).route()
        StartRoute.MOVIE  -> MovieDetailsScreenRoute(id).route()
        StartRoute.ACTOR  -> ActorDetailsScreenRoute(id).route()
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

                ActorScreen(actorId = actorId)            // no nav lambda needed
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
                    actorId      = actorId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

    }
}


enum class StartRoute {
    SERIES, MOVIE, ACTOR
}