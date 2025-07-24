package com.sanaa.presentation.screen.mediaWithTypeTabScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

interface MediaWithTypeTabScreenInteractionListener {
    fun onMediaTabSelection(mediaType: MediaType)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaType: MediaType)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
}