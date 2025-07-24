package com.sanaa.presentation.mediaListContent.getMediaStrategy

import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem

interface GetMediaStrategy {
    fun getMediaSectionTitle(): String
    suspend fun getMediaList(page:Int, genreId: Int? = null): List<MediaItem>
    suspend fun getMediaGenreList() : List<GenreUiState>
}