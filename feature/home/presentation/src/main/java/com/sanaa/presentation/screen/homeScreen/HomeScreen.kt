package com.sanaa.presentation.screen.homeScreen

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.api.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.api.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.api.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.api.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.screen.homeScreen.screenContent.HomeScreenContent
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    Log.d("stateTest", "HomeScreen: state:${state.value}")

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMediaDetails -> {
                    when (effect.mediaType) {
                        MediaType.MOVIE -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.MOVIE,
                                id = effect.id
                            )
                        }

                        MediaType.TV_SHOW -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.SERIES,
                                id = effect.id
                            )
                        }
                    }
                }

                HomeScreenEffect.NavigateToMoviesScreen -> {
                    navController.navigate(TrendingMoviesScreenRoute)
                }

                HomeScreenEffect.NavigateToPeopleScreen -> {
                    navController.navigate(TrendingPeopleScreenRoute)
                }

                HomeScreenEffect.NavigateToTopRatingMediaScreen -> {
                    navController.navigate(TopRatedMediaScreenRoute)
                }

                HomeScreenEffect.NavigateToTvShowsScreen -> {
                    navController.navigate(TrendingTvShowsScreenRoute)
                }

                HomeScreenEffect.NavigateToWatchedMediaScreen -> {
                    // TODO()
                }

            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {

        HomeScreenContent(
            state = state.value,
            interactionListener = viewModel,
        )
    }
}