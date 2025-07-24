package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.screen_state_content.ErrorStateContent
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.tab.NovixTab
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.MOVIE_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.TV_SHOW_INDEX
import com.sanaa.presentation.screen.state.TvShowUiModel
import exceptions.NoNetworkException

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
    modifier: Modifier = Modifier,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {
    val tabs = listOf(
        stringResource(R.string.movies),
        stringResource(R.string.tv_shows),
        stringResource(R.string.actors)
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(top = 12.dp)
    ) {
        NovixTab(
            categories = tabs,
            selectedIndex = selectedTabIndex,
            onCategorySelected = interactionsListener::onTabSelected,
        )

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    WavyProgressIndicator()
                }
            }

            uiState.noInternetConnection -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NetworkDisconnectionContact(onRetryClick = {})
                }
            }

            else -> {
                CategoryTabContent(
                    selectedTabIndex,
                    interactionsListener,
                    moviesPagingData = moviesPagingData,
                    tvShowsPagingData = tvShowsPagingData,
                    actorsPagingData = actorsPagingData,
                )
            }
        }
    }
}

@Composable
fun CategoryTabContent(
    selectedTabIndex: Int,
    interactionsListener: SearchScreenInteractionsListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {
    val movieState = moviesPagingData.loadState.refresh
    val isMovieEmpty = moviesPagingData.itemCount == 0 &&
            movieState !is LoadState.Loading &&
            movieState !is LoadState.Error

    val isTvEmpty = tvShowsPagingData.itemCount == 0 &&
            tvShowsPagingData.loadState.refresh !is LoadState.Loading &&
            tvShowsPagingData.loadState.refresh !is LoadState.Error

    val isActorEmpty = actorsPagingData.itemCount == 0 &&
            actorsPagingData.loadState.refresh !is LoadState.Loading &&
            actorsPagingData.loadState.refresh !is LoadState.Error

    when (selectedTabIndex) {
        MOVIE_INDEX -> {
            if (movieState is LoadState.Error) {
                ErrorState(movieState) { interactionsListener.retrySearch() }
            } else if (isMovieEmpty) {
                NoSearchResultState()
            } else {
                MoviesContent(
                    moviesPagingData = moviesPagingData,
                    onMovieClick = { recent, movie ->
                        interactionsListener.onSearchResultMediaClicked(recent)
                    }
                )
            }
        }

        TV_SHOW_INDEX -> {
            if (isTvEmpty) {
                NoSearchResultState()
            } else {
                TvShowsContent(
                    tvShowsPagingData = tvShowsPagingData,
                    onTvShowClick = { recent, tvShow ->
                        interactionsListener.onSearchResultMediaClicked(recent)
                    }
                )
            }
        }

        ACTOR_INDEX -> {
            if (isActorEmpty) {
                NoSearchResultState()
            } else {
                ActorsContent(actorsPagingData, onActorClick = {
                    interactionsListener.onActorClicked(it.id)
                })
            }
        }
    }
}

@Composable
private fun ErrorState(movieState: LoadState.Error, onRetryClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        if (movieState.error is NoNetworkException)
            NetworkDisconnectionContact(onRetryClick = onRetryClick)
        else
            ErrorStateContent(
                onRetryClick = onRetryClick,
                errorTitle = stringResource(R.string.error_general_title),
                errorMessage = stringResource(R.string.error_general_message)
            )
    }
}

@Composable
private fun NoSearchResultState() {
    EmptySearchContent(
        icon = painterResource(id = R.drawable.ic_no_search_result),
        text = stringResource(id = R.string.no_search_result_message)
    )
}
