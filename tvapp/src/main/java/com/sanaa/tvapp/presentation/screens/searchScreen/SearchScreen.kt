package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.ActorTvContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.MovieTvContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.SearchTextField
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvEmptySearchContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvErrorStateContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvShowTvContent
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvTopBar
import com.sanaa.tvapp.presentation.screens.sharedComponents.TvLoadingIndicator


@Composable
fun SearchScreen(
    searchViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = uiState.movies.collectAsLazyPagingItems()
    val tvShowsPagingData = uiState.tvShows.collectAsLazyPagingItems()
    val actorsPagingData = uiState.actors.collectAsLazyPagingItems()
    NovixTheme(isSystemInDarkTheme()) {
        SearchScreenContent(
            uiState = uiState,
            searchListener = searchViewModel,
            moviesPagingData = moviesPagingData,
            tvShowsPagingData = tvShowsPagingData,
            actorsPagingData = actorsPagingData,
        )
    }
}

@Composable
fun SearchScreenContent(
    uiState: SearchTvScreenUiState,
    searchListener: SearchScreenInteractionListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {

    var text by remember { mutableStateOf("") }

    val movieRefreshState = moviesPagingData.loadState.refresh
    val tvShowRefreshState = tvShowsPagingData.loadState.refresh
    val actorRefreshState = actorsPagingData.loadState.refresh

    val isMovieEmpty = moviesPagingData.itemCount == 0 &&
            movieRefreshState !is LoadState.Loading &&
            movieRefreshState !is LoadState.Error

    val isTvEmpty = tvShowsPagingData.itemCount == 0 &&
            tvShowRefreshState !is LoadState.Loading &&
            tvShowRefreshState !is LoadState.Error

    val isActorEmpty = actorsPagingData.itemCount == 0 &&
            actorRefreshState !is LoadState.Loading &&
            actorRefreshState !is LoadState.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TvTopBar(
            selectedTabIndex = uiState.selectedTabIndex,
            onTabSelected = searchListener::onTabSelected
        )

        SearchTextField(
            text = text,
            onTextChange = {
                text = it
                searchListener.onSearchQueryChanged(it)
            },
        )

        when (uiState.selectedTabIndex) {
            SearchTvScreenUiState.MOVIE_INDEX -> {
                when {
                    text.isBlank() -> TvEmptySearchContent()

                    movieRefreshState is LoadState.Loading -> TvLoadingIndicator()
                    movieRefreshState is LoadState.Error -> TvErrorStateContent(
                        movieRefreshState,
                        searchListener::retrySearch
                    )

                    isMovieEmpty -> TvEmptySearchContent()
                    else -> MovieTvContent(moviesPagingData) {
                        searchListener.onSearchResultMediaClicked()
                    }
                }
            }

            SearchTvScreenUiState.TV_SHOW_INDEX -> {
                when {
                    text.isBlank() -> TvEmptySearchContent()

                    tvShowRefreshState is LoadState.Loading -> TvLoadingIndicator()
                    tvShowRefreshState is LoadState.Error -> TvErrorStateContent(
                        tvShowRefreshState,
                        searchListener::retrySearch
                    )

                    isTvEmpty -> TvEmptySearchContent()
                    else -> TvShowTvContent(tvShowsPagingData) {
                        searchListener.onSearchResultMediaClicked()
                    }
                }
            }

            SearchTvScreenUiState.ACTOR_INDEX -> {
                when {
                    text.isBlank() -> TvEmptySearchContent()

                    actorRefreshState is LoadState.Loading -> TvLoadingIndicator()
                    actorRefreshState is LoadState.Error -> TvErrorStateContent(
                        actorRefreshState,
                        searchListener::retrySearch
                    )

                    isActorEmpty -> TvEmptySearchContent()
                    else -> ActorTvContent(actorsPagingData) {
                        searchListener.onActorClicked(it.id)
                    }
                }
            }
        }
    }
}