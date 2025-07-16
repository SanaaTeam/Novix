package com.sanaa.presentation.screen.series

interface SeriesScreenInteractionListener {
    fun onBackClicked()
    fun onReadMoreClicked()
    fun onViewReviewsClicked(seriesId: Int)
    fun onActorClicked(actorId: Int)
    fun onSeasonNumberClicked(seasonNumber: Int)
    fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onPlayTrailerClicked()
}