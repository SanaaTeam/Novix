package com.sanaa.presentation.screen.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.app.navigation.PlayListScreenRoute
import com.sanaa.presentation.app.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.app.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.app.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.app.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.app.navigation.WatchingMediaHistoryScreenRoute
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.screen.homeScreen.screenContent.HomeScreenContent
import com.sanaa.presentation.state.MediaTypeUi
import dagger.hilt.android.EntryPointAccessors

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val navController = LocalMainNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }
    val authApi = remember {
        EntryPointAccessors.fromApplication(
            appContext,
            HomeApiEntryPoint::class.java
        ).authenticationApi()
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    var snack by remember { mutableStateOf<SnackData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMediaDetails -> {
                    when (effect.mediaTypeUi) {
                        MediaTypeUi.MOVIE -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.MOVIE,
                                id = effect.id
                            )
                        }

                        MediaTypeUi.TV_SHOW -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.TV_SHOW,
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
                    navController.navigate(WatchingMediaHistoryScreenRoute)
                }

                HomeScreenEffect.NavigateToPlayListScreen -> {
                    navController.navigate(PlayListScreenRoute)
                }

                is HomeScreenEffect.ShowError -> {
                    snack = SnackData(message = effect.message, isError = true)
                }

                is HomeScreenEffect.ShowSuccess -> {
                    snack = SnackData(message = effect.message, isError = false)
                }
            }
        }
    }

    Box(modifier = Modifier.systemBarsPadding()) {
        HomeScreenContent(
            state = state.value,
            interactionListener = viewModel,
            authApi = authApi
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}