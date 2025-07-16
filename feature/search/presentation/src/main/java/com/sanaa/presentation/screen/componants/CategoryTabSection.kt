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
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.SearchViewModel
import com.sanaa.presentation.screen.state.SearchScreenUiState

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    searchViewModel: SearchViewModel,
    interactionsListener: SearchScreenInteractionsListener,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        stringResource(R.string.movies),
        stringResource(R.string.tv_shows),
        stringResource(R.string.actors)
    )

    val moviesPagingItems = searchViewModel.moviesPagingData.collectAsLazyPagingItems()
    val tvShowsPagingItems = searchViewModel.tvShowsPagingData.collectAsLazyPagingItems()
    val actorsPagingItems = searchViewModel.actorsPagingData.collectAsLazyPagingItems()

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
                val isMovieEmpty = moviesPagingItems.itemCount == 0 &&
                        moviesPagingItems.loadState.refresh !is androidx.paging.LoadState.Loading &&
                        moviesPagingItems.loadState.refresh !is androidx.paging.LoadState.Error

                val isTvEmpty = tvShowsPagingItems.itemCount == 0 &&
                        tvShowsPagingItems.loadState.refresh !is androidx.paging.LoadState.Loading &&
                        tvShowsPagingItems.loadState.refresh !is androidx.paging.LoadState.Error

                val isActorEmpty = actorsPagingItems.itemCount == 0 &&
                        actorsPagingItems.loadState.refresh !is androidx.paging.LoadState.Loading &&
                        actorsPagingItems.loadState.refresh !is androidx.paging.LoadState.Error

                when (selectedTabIndex) {
                    0 -> {
                        if (isMovieEmpty) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoSearchResultState()
                            }
                        } else {
                            val lazyPagingItems =
                                searchViewModel.moviesPagingData.collectAsLazyPagingItems()

                            MoviesContent(
                                moviesPagingData = lazyPagingItems,
                                onMovieClick = { interactionsListener.onSearchResultMediaClicked(it) }
                            )
                        }
                    }

                    1 -> {
                        if (isTvEmpty) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoSearchResultState()
                            }
                        } else {
                            TvShowsContent(
                                tvShowsPagingData = searchViewModel.tvShowsPagingData.collectAsLazyPagingItems(),
                                onTvShowClick = { interactionsListener.onSearchResultMediaClicked(it) }
                            )
                        }
                    }

                    2 -> {
                        if (isActorEmpty) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoSearchResultState()
                            }
                        } else {
                            val lazyPagingItems =
                                searchViewModel.actorsPagingData.collectAsLazyPagingItems()

                            ActorsContent(lazyPagingItems)
                        }
                    }
                }
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
