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
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.state.SearchScreenUiState

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf("Movies", "TV Shows", "Actors")


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
        } else when (selectedTabIndex) {

            0 -> {
                if (uiState.movies.isEmpty()) NoSearchResultState()
                else MoviesContent(uiState.movies, onMovieClick = {
                    interactionsListener.onSearchResultMediaClicked(it)
                })
            }

            1 -> {
                if (uiState.movies.isEmpty()) NoSearchResultState()
                else TvShowsContent(
                    uiState.tvShows, onTvShowClick = {
                        interactionsListener.onSearchResultMediaClicked(it)
                    })

            }

            2 -> {
                if (uiState.movies.isEmpty()) NoSearchResultState()
                else ActorsContent(uiState.actors)
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
