package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

interface MovieDetailsScreenInteractionListener {
    fun onPlayTrailerClicked()
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onRetryLoadDetails()
    fun onRateClick()
    fun onRatingChange(rating: Int)
    fun onDismissRateDialog()
    fun onSummitRateClick()
    fun onSimilarMovieClick(movieId: Int)
    fun onActorCardClick(actorId: Int)
    fun onSnackDismissRequested()
}