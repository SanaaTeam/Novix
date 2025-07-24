package com.sanaa.presentation.mediaListContent.getMediaStrategy

import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem

class MediaProvider(
    private val getMediaStrategy: GetMediaStrategy
) {

    fun getMediaSectionTitle(): String {
        return getMediaStrategy.getMediaSectionTitle()
    }

    suspend fun getMediaList(page: Int, genreId: Int? = null): List<MediaItem> {
        return getMediaStrategy.getMediaList(page, genreId)
    }

    suspend fun getMediaGenreList(): List<GenreUiState> {
        return getMediaStrategy.getMediaGenreList()
    }
}