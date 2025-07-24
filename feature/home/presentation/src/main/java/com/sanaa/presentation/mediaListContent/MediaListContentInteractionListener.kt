package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem

data class MediaListSectionUiState (
    val title: String? = null,
    val mediaList: List<MediaItem> = emptyList(),
    val genreList: List<GenreUiState> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)

interface MediaListSectionInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(media: MediaItem)
}