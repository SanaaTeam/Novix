package com.sanaa.presentation.app

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.api.navigation.AppNavigation
import com.sanaa.presentation.api.navigation.AppRoute
import com.sanaa.presentation.api.navigation.ContinueWatchingMediaScreenRoute
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.api.navigation.MainScreenRoute
import com.sanaa.presentation.api.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.api.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.api.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.api.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesScreen
import com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen.ContinueWatchingMediaScreen
import com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.TopRatedMediaScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen.TrendingMoviesScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen.TrendingTvShowsScreen

@Composable
fun NovixApp(

) {
    val appNavController = rememberNavController()
    Box {
        CompositionLocalProvider(LocalAppNavController provides appNavController) {
            AppNavigation(
                startDestination = MainScreenRoute, modifier = Modifier
            )
        }
    }
}

@Composable
private fun AppNavigation(
    startDestination: AppRoute,
    modifier: Modifier = Modifier,
) {
    NovixTheme(isSystemInDarkTheme()) {
        NavHost(
            modifier = modifier.background(color = Theme.colors.surface),
            navController = AppNavigation.app,
            startDestination = startDestination,
        ) {
            composable<MainScreenRoute> {
                MainScreen()
            }

            composable<TrendingMoviesScreenRoute> {
                TrendingMoviesScreen()
            }

            composable<TrendingTvShowsScreenRoute> {
                TrendingTvShowsScreen()
            }

            composable<TrendingPeopleScreenRoute> {
                CelebritiesScreen()
            }

            composable<TopRatedMediaScreenRoute> {
                TopRatedMediaScreen()
            }

            composable<ContinueWatchingMediaScreenRoute> {
                ContinueWatchingMediaScreen()
            }
        }
    }
}