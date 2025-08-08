package com.sanaa.tvapp.presentation.screens

import com.sanaa.tvapp.state.MediaItem
import com.sanaa.tvapp.state.MediaTypeUi

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