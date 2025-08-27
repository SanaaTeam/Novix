package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

sealed interface TvShowDetailsScreenEffect {
    data class NavigateToActorScreen(val actorId: Int) : TvShowDetailsScreenEffect
    data class NavigateToEpisodeDetailsScreen(
        val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int,
    ) : TvShowDetailsScreenEffect

    data class PlayTrailer(val trailerUrl: String?) : TvShowDetailsScreenEffect
    object NavigateToLogin : TvShowDetailsScreenEffect
    object UpdateRate : TvShowDetailsScreenEffect
}