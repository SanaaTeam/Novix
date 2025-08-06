package com.sanaa.presentation.app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.sanaa.presentation.api.navigation.WatchingHistoryScreenRoute
import com.sanaa.presentation.providers.LocalSafeContentThreshold
import com.sanaa.presentation.providers.LocalThemeProvider
import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesScreen
import com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen.ContinueWatchingMediaScreen
import com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.TopRatedMediaScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen.TrendingMoviesScreen
import com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen.TrendingTvShowsScreen
import com.sanaa.presentation.screen.WatchingHistoryScreen

@Composable
fun NovixApp(
    viewModel: NovixAppViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    val appNavController = rememberNavController()
    Box {
        CompositionLocalProvider(
            LocalAppNavController provides appNavController,
            LocalThemeProvider provides state.value.isDarkTheme,
            LocalSafeContentThreshold provides state.value.safeContentThreshold,
        ) {
            AppNavigation(
                startDestination = MainScreenRoute,
                state = state.value,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun AppNavigation(
    startDestination: AppRoute,
    state: NovixAppUiState,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val activity = view.context as? ComponentActivity

    LaunchedEffect(state.isDarkTheme) {
        activity?.window?.also { window ->
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = !state.isDarkTheme
                isAppearanceLightNavigationBars = !state.isDarkTheme
            }
        }
    }
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

        composable<WatchingHistoryScreenRoute> {
            WatchingHistoryScreen()
        }
    }
}
