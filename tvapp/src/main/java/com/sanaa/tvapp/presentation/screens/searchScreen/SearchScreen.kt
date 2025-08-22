package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.home.component.SearchTaps
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.AnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.HandlePagingState
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.MovieTvContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.SearchTextField
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvEmptySearchContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvShowTvContent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    searchViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = uiState.movies.collectAsLazyPagingItems()
    val tvShowsPagingData = uiState.tvShows.collectAsLazyPagingItems()
    val actorsPagingData = uiState.actors.collectAsLazyPagingItems()
    val navController = LocalAppNavController.current

    EffectHandler(
        effect = searchViewModel.effect,
        navController = navController
    )

        SearchScreenContent(
            uiState = uiState,
            interactionListener = searchViewModel,
            moviesPagingData = moviesPagingData,
            tvShowsPagingData = tvShowsPagingData,
            actorsPagingData = actorsPagingData,
        )

}
@Composable
private fun SearchScreenContent(
    uiState: SearchTvScreenUiState,
    interactionListener: SearchScreenInteractionListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.surface),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchTextField(
                text = uiState.searchQuery,
                onTextChange = interactionListener::onSearchQueryChanged,
            )

            SearchTaps(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                sidePaddings = 36.dp,
                { interactionListener.onTabSelected(it) }
            )

            AnimatedContent(
                targetState = uiState,
                label = "search_content"
            ) { state ->
                when {
                    state.noInternetConnection -> NetworkDisconnectionContact(
                        onRetryClick = { interactionListener.onRetryClicked() },
                        modifier = Modifier.fillMaxSize(),
                    )

                    state.searchQuery.isNotBlank() -> {
                        when (state.selectedTabIndex) {
                            SearchTvScreenUiState.MOVIE_INDEX -> {
                                HandlePagingState(moviesPagingData, interactionListener) {
                                    MovieTvContent(moviesPagingData) { movieId ->
                                        interactionListener.onMovieClicked(movieId)
                                    }
                                }
                            }

                            SearchTvScreenUiState.TV_SHOW_INDEX -> {
                                HandlePagingState(tvShowsPagingData, interactionListener) {
                                    TvShowTvContent(tvShowsPagingData) { tvShowId ->
                                        interactionListener.onTvShowClicked(tvShowId)
                                    }
                                }
                            }

                            SearchTvScreenUiState.ACTOR_INDEX -> {
                                HandlePagingState(actorsPagingData, interactionListener) {
                                    TvContent(actorsPagingData) { actor ->
                                        interactionListener.onActorClicked(actor.id)
                                    }
                                }
                            }
                        }
                    }

                    state.isLoading -> LoadingIndicator()

                    else -> TvEmptySearchContent()
                }
            }
        }

        if (uiState.snackBarData != null) {
            AnimatedSnackBarHost(
                data = uiState.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<SearchScreenEffect>,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        effect.collectLatest {
            when (it) {
                is SearchScreenEffect.NavigateToActorDetails -> navController.navigate(
                    ScreensRoute.ActorDetailsRoute(it.id)
                )

                is SearchScreenEffect.NavigateToMovieDetails -> navController.navigate(
                    ScreensRoute.MovieDetailsRoute(it.id)
                )

                is SearchScreenEffect.NavigateToTvShowDetails -> navController.navigate(
                    ScreensRoute.TvShowDetailsRoute(it.id)
                )
            }
        }
    }
}
