package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun DetailsNavHost(startRoute: StartRoute, id: Int) {
    val initialRoute = when (startRoute) {
        StartRoute.SERIES -> SeriesDetailsScreenRoute(id).route()
        StartRoute.MOVIE -> SeriesDetailsScreenRoute(id).route()
        StartRoute.ACTOR -> SeriesDetailsScreenRoute(id).route()
    }
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavControllerProvider provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = initialRoute,
        ) {
        }
    }
}


enum class StartRoute {
    SERIES, MOVIE, ACTOR
}