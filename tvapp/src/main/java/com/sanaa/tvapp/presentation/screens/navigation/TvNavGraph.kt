package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import com.sanaa.tvapp.presentation.screens.login.LoginScreenTv
import com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen.MovieDetailsScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.TvShowScreen
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreen
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider

@Composable
fun TvNavGraph(navController: NavHostController, startDestination: Any) {
    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable<NavBarRoute.Home> { HomeScreen() }
            composable<NavBarRoute.Search> { SearchScreen() }
            composable<ScreensRoute.Login> {
                LoginScreenTv(
                    onFinish = {
                        navController.navigate(NavBarRoute.Home) {
                            popUpTo(ScreensRoute.Login) { inclusive = true }
                        }
                    }
                )
            }
            composable<NavBarRoute.Categories> { }
            composable<NavBarRoute.MyList> { }
            composable<NavBarRoute.MyAccount> { }
            composable<ScreensRoute.MovieDetails> { navBackStackEntry ->
                val movieId = navBackStackEntry.toRoute<ScreensRoute.MovieDetails>().movieId
                MovieDetailsScreen(movieId)
            }
            composable<ScreensRoute.TvShowDetails> { navBackStackEntry ->
                val tvShowId = navBackStackEntry.toRoute<ScreensRoute.TvShowDetails>().tvShowId
                TvShowScreen(tvShowId)
            }
        }
    }
}