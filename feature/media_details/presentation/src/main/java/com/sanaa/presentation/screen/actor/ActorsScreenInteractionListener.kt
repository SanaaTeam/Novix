package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.model.MovieUiModel

interface ActorsScreenInteractionListener {
    fun onBackClicked()
    fun onTopMoviesClicked()
    fun onTopSeriesClicked()
    fun onViewAllGalleryClicked()
    fun onSeriesClicked(id: Int)
    fun onMovieClicked(id: Int)
    fun onDismissBottomSheet()
    fun onSaveClicked(movie: MovieUiModel)
    fun onRetryClicked()
    fun onLoginButtonClick()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
}