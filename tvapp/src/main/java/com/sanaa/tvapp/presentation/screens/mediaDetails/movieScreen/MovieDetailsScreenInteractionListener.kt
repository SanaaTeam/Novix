package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import com.sanaa.presentation.model.GenreUiModel

interface MovieDetailsScreenInteractionListener {
    fun onBackClick()
    fun onWatchTrailerClick()
    fun onSimilarMovieClick(movieId: Int)
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onActorCardClick(actorId: Int)
    fun onRetryLoadDetails()
}