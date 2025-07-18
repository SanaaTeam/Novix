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
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    uiState: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
    onMovieClick: (MovieUiModel) -> Unit,
    onTvShowClick: (TvShowUiModel) -> Unit,
    onActorClick: (ActorUiModel) -> Unit,
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
        } else when (selectedTabIndex) {
            0 -> {
                if (uiState.movies.isEmpty()) NoSearchResultState()
                else MoviesContent(uiState.movies, onMovieClick = { recent, movie ->
                    onMovieClick(movie)
                    interactionsListener.onSearchResultMediaClicked(recent)
                })
            }


            1 -> {
                if (uiState.tvShows.isEmpty()) NoSearchResultState()
                else TvShowsContent(
                    uiState.tvShows, onTvShowClick = { recent, tvShow ->
                        onTvShowClick(tvShow)
                        interactionsListener.onSearchResultMediaClicked(recent)
                    })

            }

            2 -> {
                if (uiState.actors.isEmpty()) NoSearchResultState()
                else ActorsContent(uiState.actors, onActorClick = {
                    onActorClick(it)
                })
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
