package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.foundation.lazy.list.TvLazyRow
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.SearchTextField
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvTopBar


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
            modifier = Modifier
        )

        when (uiState.selectedTabIndex) {
            SearchTvScreenUiState.MOVIE_INDEX -> {
                TvLazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(moviesPagingData.itemCount) { index ->
                        val movie = moviesPagingData[index]
                        if (movie != null) {
                            FocusableMediaCard(
                                imageUrl = movie.imageUrl,
                                titleText = movie.title,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { /* your click handler */ }
                            )
                        }
                    }
                }
            }

            SearchTvScreenUiState.TV_SHOW_INDEX -> {
                TvLazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(tvShowsPagingData.itemCount) { index ->
                        val show = tvShowsPagingData[index]
                        if (show != null) {
                            FocusableMediaCard(
                                imageUrl = show.imageUrl,
                                titleText = show.title,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { /* your click handler */ }
                            )
                        }
                    }
                }
            }

            SearchTvScreenUiState.ACTOR_INDEX -> {
                TvLazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(actorsPagingData.itemCount) { index ->
                        val actor = actorsPagingData[index]
                        if (actor != null) {
                            FocusableMediaCard(
                                imageUrl = actor.imageUrl,
                                titleText = actor.name,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { /* your click handler */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
