package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

sealed interface TvShowDetailsScreenEffects {
    data class NavigateToActorScreen(val actorId: Int) : TvShowDetailsScreenEffects
    data class NavigateToEpisodeDetailsScreen(
        val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int,
    ) : TvShowDetailsScreenEffects

    data class PlayTrailer(val trailerUrl: String?) : TvShowDetailsScreenEffects
    object NavigateToLogin : TvShowDetailsScreenEffects
}