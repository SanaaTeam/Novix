package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
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
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator
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
                    NetworkDisconnectionContact(onRetryClick = { interactionsListener.retrySearch() })
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

    when (selectedTabIndex) {
        MOVIE_INDEX -> {
            if (movieRefreshState is LoadState.Error) {
                ErrorState(movieRefreshState) { interactionsListener.retrySearch() }
            } else if (isMovieEmpty) {
                NoSearchResultContent()
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
            if (tvShowRefreshState is LoadState.Error) {
                ErrorState(tvShowRefreshState) { interactionsListener.retrySearch() }
            } else if (isTvEmpty) {
                NoSearchResultContent()
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
            if (actorRefreshState is LoadState.Error) {
                ErrorState(actorRefreshState) { interactionsListener.retrySearch() }
            } else if (isActorEmpty) {
                NoSearchResultContent()
            } else {
                ActorsContent(actorsPagingData, onActorClick = {
                    interactionsListener.onActorClicked(it.id)
                })
            }
        }
    }
}

@Composable
private fun ErrorState(loadStateError: LoadState.Error, onRetryClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        if (loadStateError.error is NoNetworkException) {
            NetworkDisconnectionContact(onRetryClick = onRetryClick)
        } else {
            ErrorStateContent(
                onRetryClick = onRetryClick,
                errorTitle = stringResource(R.string.error_general_title),
                errorMessage = stringResource(R.string.error_general_message)
            )
        }
    }
}

@Composable
private fun NoSearchResultContent() {
    val isDarkTheme = isSystemInDarkTheme()
    val iconRss =if (isDarkTheme)
      R.drawable.ic_no_search_result_dark
        else {
      R.drawable.ic_no_search_result
    }
    EmptySearchContent(
        icon =painterResource(id = iconRss),
        text = stringResource(id = R.string.no_search_result_message)
    )
}