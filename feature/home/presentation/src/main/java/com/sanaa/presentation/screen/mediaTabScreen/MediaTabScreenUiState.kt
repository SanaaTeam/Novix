package com.sanaa.presentation.screen.mediaTabScreen

import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

data class MediaTabScreenUiState (
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val movieList: List<MediaItem> = emptyList(),
    val tvShowList: List<MediaItem> = emptyList(),
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvShowGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val tvShowSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)