package com.sanaa.presentation.screen.episodeDetails

interface EpisodeDetailsInteractionListener {
    fun onBackClick()
    fun onPlayTrailerClick()
    fun onGenreTypeClick(genreId: Int)
    fun onCastClick(actorId: Int)
    fun onSavedClick(tvShowId: Int)
    fun onDismissBottomSheet()
    fun onLoginButtonClick()
    fun onRetryLoadDetails()
    fun onSnackDismissRequested()
}