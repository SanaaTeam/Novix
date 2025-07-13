package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.R
import com.sanaa.presentation.screen.CategoryTab
import com.sanaa.presentation.state.SearchScreenUiState

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    uiState: SearchScreenUiState,
) {
    val tabs = listOf("Movies", "TV Shows", "Actors")

    Column {
        CategoryTab(
            categories = tabs,
            selectedIndex = selectedTabIndex,
            onCategorySelected = onTabSelected
        )

        if (uiState.noInternetConnection) {
            NetworkDisconnectionContact (onRetryClick = {})
            return
        }

        when (selectedTabIndex) {
            0 -> {
                if (uiState.searchQuery.isEmpty())
                    DefaultState(uiState)
                else if (uiState.movies.isEmpty())
                    NoSearchResultState()
                else
                    MoviesContent(uiState.movies)
            }

            1 -> {
                if (uiState.searchQuery.isEmpty())
                    DefaultState(uiState)
                else if (uiState.movies.isEmpty())
                    NoSearchResultState()
                else
                    TvShowsContent(uiState.tvShows)

            }

            2 -> {
                if (uiState.searchQuery.isEmpty())
                    DefaultState(uiState)
                else if (uiState.movies.isEmpty())
                    NoSearchResultState()
                else
                    ActorsContent(uiState.actors)
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

@Composable
private fun DefaultState(uiState: SearchScreenUiState) {
    val showEmptyMessage = uiState.resentSearchTitleList.isEmpty()
    if (showEmptyMessage) {
        EmptySearchState(
            icon = painterResource(id = R.drawable.empty_search),
            text = stringResource(id = R.string.empty_search_message)
        )
    } else {
        SearchHistoryContent(
            recentViewed = uiState.resentViewedImageList,
            recentSearches = uiState.resentSearchTitleList
        )
    }
}