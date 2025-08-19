package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sanaa.tvapp.presentation.screens.category.CategoriesScreen
import com.sanaa.tvapp.presentation.screens.genreMovies.GenreMoviesScreen
import com.sanaa.tvapp.presentation.screens.genreTvShows.GenreTvShowsScreen
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen.ActorScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen.EpisodeDetailsScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen.MovieDetailsScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.TvShowScreen
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreen
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreMovieScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreTvShowsScreenRoute
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreen

@Composable
fun TvNavGraph(navController: NavHostController, startDestination: Any) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<NavBarRoute.Home> { HomeScreen() }
        composable<NavBarRoute.Search> { SearchScreen() }
        composable<NavBarRoute.Categories> { CategoriesScreen() }
        composable<NavBarRoute.MyAccount> { MyAccountScreen() }
        composable<ScreensRoute.MovieDetailsRoute> { navBackStackEntry ->
            MovieDetailsScreen()
        }
        composable<ScreensRoute.TvShowDetailsRoute> { navBackStackEntry ->
            TvShowScreen()
        }
        composable<ScreensRoute.EpisodeDetailsRoute> { navBackStackEntry ->
            EpisodeDetailsScreen()
        }

        composable<ScreensRoute.ActorDetailsRoute> { navBackStackEntry ->
            ActorScreen()
        }

        composable<GenreMovieScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GenreMovieScreenRoute>()
            GenreMoviesScreen()
        }

        composable<GenreTvShowsScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GenreTvShowsScreenRoute>()
            GenreTvShowsScreen()
        }
    }
}