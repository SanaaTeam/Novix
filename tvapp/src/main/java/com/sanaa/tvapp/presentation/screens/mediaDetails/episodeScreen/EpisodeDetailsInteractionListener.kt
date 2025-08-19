package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

interface EpisodeDetailsInteractionListener {
    fun onPlayTrailerClick()
    fun onGenreTypeClick(genreId: Int)
    fun onCastClick(actorId: Int)
    fun onLoginButtonClick()
    fun onRetryLoadDetails()
    fun onRatingChanged(newRating: Int)
    fun onDismissRateBottomSheet()
}