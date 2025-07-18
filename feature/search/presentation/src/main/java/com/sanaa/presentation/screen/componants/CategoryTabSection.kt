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
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.MOVIE_INDEX
import com.sanaa.presentation.screen.state.SearchScreenUiState.Companion.TV_SHOW_INDEX

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        stringResource(R.string.movies),
        stringResource(R.string.tv_shows),
        stringResource(R.string.actors)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.padding(top = 12.dp)
    ) {
        CategoryTab(
            categories = tabs,
            selectedIndex = selectedTabIndex,
            onCategorySelected = interactionsListener::onTabSelected,
        )
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WavyProgressIndicator()
            }
        } else if (uiState.noInternetConnection) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NetworkDisconnectionContact(onRetryClick = {})
            }
        } else {
            CategoryTabContent(selectedTabIndex, uiState, interactionsListener)
        }
    }
}

@Composable
fun CategoryTabContent(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
) {
    when (selectedTabIndex) {
        MOVIE_INDEX -> {
            if (uiState.movies.isEmpty()) {
                NoSearchResultState()
                return
            }

            MoviesContent(uiState.movies, onMovieClick = {
                interactionsListener.onSearchResultMediaClicked(it)
            })
        }

        TV_SHOW_INDEX -> {
            if (uiState.tvShows.isEmpty()) {
                NoSearchResultState()
                return
            }

            TvShowsContent(uiState.tvShows, onTvShowClick = {
                interactionsListener.onSearchResultMediaClicked(it)
            })
        }

        ACTOR_INDEX -> {
            if (uiState.actors.isEmpty()) {
                NoSearchResultState()
                return
            }

            ActorsContent(uiState.actors)
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
