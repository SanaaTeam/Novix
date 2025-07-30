package com.sanaa.presentation.screen.homeScreen

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    mediaDetailsApi: MediaDetailsApi,
) {

    val navController = LocalAppNavController.current

    val state = viewModel.state.collectAsStateWithLifecycle()
    val effect: HomeScreenEffect? by viewModel.effect.collectAsStateWithLifecycle(null)
    Log.d("stateTest", "HomeScreen: state:${state.value}")

    LaunchedEffect(effect) {
        when (effect) {
            is HomeScreenEffect.NavigateToMediaDetails -> {
                when ((effect as HomeScreenEffect.NavigateToMediaDetails).mediaType) {
                    MediaType.MOVIE -> {
                        mediaDetailsApi.launch(
                            context = navController.context,
                            startRoute = StartRoute.MOVIE,
                            id = (effect as HomeScreenEffect.NavigateToMediaDetails).id
                        )
                    }

                    MediaType.TV_SHOW -> {
                        mediaDetailsApi.launch(
                            context = navController.context,
                            startRoute = StartRoute.SERIES,
                            id = (effect as HomeScreenEffect.NavigateToMediaDetails).id
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

            null -> {}
        }
    }

    NovixTheme(isSystemInDarkTheme()) {

        HomeScreenContent(
            state = state.value,
            interactionListener = viewModel,
        )
    }
}