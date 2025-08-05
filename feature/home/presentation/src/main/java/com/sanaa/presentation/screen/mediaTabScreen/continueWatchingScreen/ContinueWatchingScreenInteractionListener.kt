package com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface ContinueWatchingScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
}