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
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.api.navigation.AppNavigation
import com.sanaa.presentation.api.navigation.AppRoute
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.api.navigation.MainScreenRoute
import com.sanaa.presentation.api.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.api.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.api.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.api.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesScreen
import com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.TopRatedMediaScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen.TrendingMoviesScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen.TrendingTvShowsScreen

@Composable
fun NovixApp(
    homeFeatureApi: HomeFeatureApi,
    searchFeatureApi: SearchFeatureApi,
    mediaDetailsApi: MediaDetailsApi
) {
    val appNavController = rememberNavController()
    Box {
        CompositionLocalProvider(LocalAppNavController provides appNavController) {
            AppNavigation(
                homeFeatureApi = homeFeatureApi,
                searchFeatureApi = searchFeatureApi,
                mediaDetailsApi = mediaDetailsApi,
                startDestination = MainScreenRoute,
                modifier = Modifier
            )
        }
    }
}


@Composable
private fun AppNavigation(
    homeFeatureApi: HomeFeatureApi,
    searchFeatureApi: SearchFeatureApi,
    mediaDetailsApi: MediaDetailsApi,
    startDestination: AppRoute,
    modifier: Modifier = Modifier
) {
    NovixTheme(isSystemInDarkTheme()) {
        NavHost(
            modifier = modifier.background(color = Theme.colors.surface),
            navController = AppNavigation.app,
            startDestination = startDestination,
        ) {
            composable<MainScreenRoute> {
                MainScreen(
                    homeFeatureApi = homeFeatureApi,
                    searchFeatureApi = searchFeatureApi,
                )
            }

            composable<TrendingMoviesScreenRoute> {
                TrendingMoviesScreen(mediaDetailsApi = mediaDetailsApi)
            }

            composable<TrendingTvShowsScreenRoute> {
                TrendingTvShowsScreen(mediaDetailsApi = mediaDetailsApi)
            }

            composable<TrendingPeopleScreenRoute> {
                CelebritiesScreen(mediaDetailsApi = mediaDetailsApi)
            }

            composable<TopRatedMediaScreenRoute> {
                TopRatedMediaScreen(mediaDetailsApi = mediaDetailsApi)
            }
        }
    }
}