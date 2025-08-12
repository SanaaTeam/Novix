package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

interface MovieDetailsScreenInteractionListener {
    fun onWatchTrailerClick()
    fun onSimilarMovieClick(movieId: Int)
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onActorCardClick(actorId: Int)
    fun onRetryLoadDetails()
}