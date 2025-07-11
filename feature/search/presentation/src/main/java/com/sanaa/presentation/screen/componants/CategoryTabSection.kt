package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.sanaa.presentation.screen.CategoryTab
import com.sanaa.presentation.state.SearchScreenUiState

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    uiState: SearchScreenUiState
) {
    val tabs = listOf("Movies", "TV Shows", "Actors")

    Column {
        CategoryTab(
            categories = tabs,
            selectedIndex = selectedTabIndex,
            onCategorySelected = { onTabSelected }
        )

        when (selectedTabIndex) {
            0 -> MoviesContent(uiState.movies)
            1 -> TvShowsContent(uiState.tvShows)
            2 -> ActorsContent(uiState.actors)
        }
    }
}