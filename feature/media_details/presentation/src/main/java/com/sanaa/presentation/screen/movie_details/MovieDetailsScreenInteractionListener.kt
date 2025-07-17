package com.sanaa.presentation.screen.movie_details

interface MovieDetailsScreenInteractionListener {
    fun onLoadMovieDetails(movieId: Int)
    fun onBackClick()
    fun onWatchTrailerClick()
    fun onReadMoreClick()
    fun onBookmarkClick(movieId: Int)
}