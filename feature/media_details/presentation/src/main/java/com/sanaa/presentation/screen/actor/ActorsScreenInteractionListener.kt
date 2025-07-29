package com.sanaa.presentation.screen.actor

interface ActorsScreenInteractionListener {
    fun onBackClicked()
    fun onTopMoviesClicked()
    fun onTopSeriesClicked()
    fun onViewAllGalleryClicked()
    fun onSeriesClicked(id: Int)
    fun onMovieClicked(id: Int)
    fun onDismissBottomSheet()

    // TODO TAKE THE ID AND SAVE MOVIE
    fun onSaveClicked()
    fun onRetryClicked()
    fun onLoginButtonClick()
}