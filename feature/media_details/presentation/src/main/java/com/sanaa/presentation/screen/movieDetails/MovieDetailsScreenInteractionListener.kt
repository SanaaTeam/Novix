package com.sanaa.presentation.screen.movieDetails

import com.sanaa.presentation.model.GenreUiModel

interface MovieDetailsScreenInteractionListener {
    fun onBackClick()
    fun onWatchTrailerClick()
    fun onReadMoreClick()
    fun onBookmarkClick(movieId: Int)
    fun onSimilarMovieClick(movieId: Int)
    fun onRateMovieClick()
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onActorCardClick(actorId: Int)
    fun onShowReviewsClick(movieId: Int)
    fun onGenreClicked(genre: GenreUiModel)
    fun onRetryLoadDetails()
}