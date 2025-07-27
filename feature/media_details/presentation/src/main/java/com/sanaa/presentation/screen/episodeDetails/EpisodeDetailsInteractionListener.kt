package com.sanaa.presentation.screen.episodeDetails

interface EpisodeDetailsInteractionListener {
    fun onBackClick()
    fun onPlayTrailerClick()
    fun onGenreTypeClick(genreId: Int)
    fun onCastClick(actorId: Int)
    fun onSavedClick(seriesId: Int)
    fun onDismissBottomSheet()
    fun onRateClicked()
    fun onLoginButtonClick()
}