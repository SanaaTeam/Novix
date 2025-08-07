package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface HomeScreenInteractionListener {
    fun onMoviesCardClicked()
    fun onTvShowsCardClicked()
    fun onPeopleCardClicked()

    fun onShowAllTopRatingClicked()
    fun onShowAllContinueWatchingClicked()


    fun onMovieGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onDismissBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onRetryClick()
}