package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface HomeScreenInteractionListener {
    fun onMoviesCardClick()
    fun onTvShowsCardClick()
    fun onPeopleCardClick()

    fun onShowAllTopRatingClick()
    fun onShowAllContinueWatchingClick()

    fun onMovieGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onDismissBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onRetryClick()
}