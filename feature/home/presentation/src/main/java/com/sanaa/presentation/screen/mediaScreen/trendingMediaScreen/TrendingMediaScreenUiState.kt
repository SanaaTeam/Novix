package com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen

import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem

data class TrendingMediaScreenUiState (
    val mediaList: List<MediaItem> = emptyList(),
    val genreList: List<GenreUiState> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)