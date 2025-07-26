package com.sanaa.presentation.screen.mediaTabScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

interface MediaTabScreenInteractionListener {
    fun onMediaTabSelection(mediaType: MediaType)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaType: MediaType)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
}