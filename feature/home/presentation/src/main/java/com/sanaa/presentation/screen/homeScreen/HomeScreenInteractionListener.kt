package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

interface HomeScreenInteractionListener {
    fun onMoviesCardClicked()
    fun onTvShowsCardClicked()
    fun onPeopleCardClicked()

    fun onShowAllTopRatingClicked()
    fun onShowAllContinueWatchingClicked()


    fun onMovieGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaType: MediaType)
    fun onSaveIconClick(media: MediaItem)

    fun onLoading()
    fun onRetryClick()
}