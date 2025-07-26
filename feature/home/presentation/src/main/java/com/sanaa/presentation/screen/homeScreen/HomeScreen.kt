package com.sanaa.presentation.screen.homeScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.api.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.api.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.api.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.api.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.screen.homeScreen.screenContent.HomeScreenContent
import com.sanaa.presentation.state.MediaType
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
    viewModel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>()
) {
    val navController = LocalAppNavController.current

    val state = viewModel.state.collectAsStateWithLifecycle()
    val effect: HomeScreenEffect? by viewModel.effect.collectAsStateWithLifecycle(null)

    LaunchedEffect(effect) {
        when(effect){
            is HomeScreenEffect.NavigateToMediaDetails -> {
                when((effect as HomeScreenEffect.NavigateToMediaDetails).mediaType){
                    MediaType.MOVIE ->{
                        onMediaClick(StartRoute.MOVIE, (effect as HomeScreenEffect.NavigateToMediaDetails).id)
                    }
                    MediaType.TV_SHOW -> {
                        onMediaClick(StartRoute.SERIES, (effect as HomeScreenEffect.NavigateToMediaDetails).id)
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
        AnimatedContent(
            targetState = state.value.popularMedia.isNotEmpty(),
            label = "PopularMediaVisibility"
        ) { hasContent ->
            if (hasContent) {
                HomeScreenContent(
                    state = state.value,
                    interactionListener = viewModel,
                    modifier = modifier,
                )
            }
        }
    }
}