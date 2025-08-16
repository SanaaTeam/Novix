package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen.ActorScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen.EpisodeDetailsScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen.MovieDetailsScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.TvShowScreen
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreen
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreen

@Composable
fun TvNavGraph(navController: NavHostController, startDestination: Any) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<NavBarRoute.Home> { HomeScreen() }
        composable<NavBarRoute.Search> { SearchScreen() }
        composable<NavBarRoute.Categories> { }
        composable<NavBarRoute.MyAccount> { MyAccountScreen() }
        composable<ScreensRoute.MovieDetails> { navBackStackEntry ->
            MovieDetailsScreen()
        }
        composable<ScreensRoute.TvShowDetails> { navBackStackEntry ->
            TvShowScreen()
        }
        composable<ScreensRoute.EpisodeDetails> { navBackStackEntry ->
            EpisodeDetailsScreen()
        }

        composable<ScreensRoute.ActorDetails> { navBackStackEntry ->
            val actorId = navBackStackEntry.toRoute<ScreensRoute.ActorDetails>().actorId
            ActorScreen()
        }
    }
}