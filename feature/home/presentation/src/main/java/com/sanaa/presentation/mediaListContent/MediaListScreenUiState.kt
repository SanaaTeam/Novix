package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem

data class MediaListScreenUiState (
    val title: String = "",
    val mediaList: List<MediaItem> = emptyList(),
    val genreList: List<GenreUiState> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)