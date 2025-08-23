package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

interface TvShowScreenInteractionListener {
    fun onActorClicked(actorId: Int)
    fun onSeasonNumberClicked(seasonNumber: Int)
    fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onPlayTrailerClicked()
    fun onRetryLoadDetails()
    fun onRateClick()
    fun onRatingChange(rating:Int)
    fun onDismissRateDialog()
    fun onSummitRateClick()
    fun onLoginButtonClick()
    fun onDismissLoginDialog()
}