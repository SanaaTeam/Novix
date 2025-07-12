package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.sanaa.presentation.screen.CategoryTab
import com.sanaa.presentation.state.SearchScreenUiState

@Composable
fun CategoryTabSection(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    uiState: SearchScreenUiState
) {
    val tabs = listOf("Movies", "TV Shows", "Actors")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CategoryTab(
            categories = tabs,
            selectedIndex = selectedTabIndex,
            onCategorySelected = onTabSelected,
        )

        when (selectedTabIndex) {
            0 -> MoviesContent(uiState.movies)
            1 -> TvShowsContent(uiState.tvShows)
            2 -> ActorsContent(uiState.actors)
        }
    }
}