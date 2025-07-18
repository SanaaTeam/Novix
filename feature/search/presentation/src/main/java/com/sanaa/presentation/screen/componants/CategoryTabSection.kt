package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.MOVIE_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.TV_SHOW_INDEX
import com.sanaa.presentation.screen.state.TvShowUiModel

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
        CategoryTab(
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
    val isMovieEmpty = moviesPagingData.itemCount == 0 &&
            moviesPagingData.loadState.refresh !is androidx.paging.LoadState.Loading &&
            moviesPagingData.loadState.refresh !is androidx.paging.LoadState.Error

    val isTvEmpty = tvShowsPagingData.itemCount == 0 &&
            tvShowsPagingData.loadState.refresh !is androidx.paging.LoadState.Loading &&
            tvShowsPagingData.loadState.refresh !is androidx.paging.LoadState.Error

    val isActorEmpty = actorsPagingData.itemCount == 0 &&
            actorsPagingData.loadState.refresh !is androidx.paging.LoadState.Loading &&
            actorsPagingData.loadState.refresh !is androidx.paging.LoadState.Error

    when (selectedTabIndex) {
        MOVIE_INDEX -> {
            if (isMovieEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoSearchResultState()
                }
            } else {
                MoviesContent(
                    moviesPagingData = moviesPagingData,
                    onMovieClick = { interactionsListener.onSearchResultMediaClicked(it) }
                )
            }
        }

        TV_SHOW_INDEX -> {
            if (isTvEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoSearchResultState()
                }
            } else {
                TvShowsContent(
                    tvShowsPagingData = tvShowsPagingData,
                    onTvShowClick = { interactionsListener.onSearchResultMediaClicked(it) }
                )
            }
        }

        ACTOR_INDEX -> {
            if (isActorEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoSearchResultState()
                }
            } else {
                ActorsContent(actorsPagingData)
            }
        }
    }
}

@Composable
private fun NoSearchResultState() {
    EmptySearchState(
        icon = painterResource(id = R.drawable.empty_search),
        text = stringResource(id = R.string.no_search_result_message)
    )
}
