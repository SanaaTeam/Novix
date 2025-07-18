package com.sanaa.presentation.screen.movie_details

interface MovieDetailsScreenInteractionListener {
    fun onBackClick()
    fun onWatchTrailerClick()
    fun onReadMoreClick()
    fun onBookmarkClick(movieId: Int)
    fun onSimilarMovieClick(movieId: Int)
    fun onRateMovieClick()
    fun onDismissLoginBottomSheet()
    fun onActorCardClick(actorId: Int)
    fun onShowReviewsClick(movieId: Int)
}