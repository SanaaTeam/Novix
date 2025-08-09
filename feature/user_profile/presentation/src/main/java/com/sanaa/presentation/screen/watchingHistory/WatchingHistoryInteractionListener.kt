package com.sanaa.presentation.screen.watchingHistory

import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi

interface WatchingHistoryInteractionListener {
    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi)
    fun onMovieGenreClick(genreId: Int?)
    fun onTvShowGenreClick(genreId: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(mediaItem: MediaItemUiModel)
    fun onBackClick()
}