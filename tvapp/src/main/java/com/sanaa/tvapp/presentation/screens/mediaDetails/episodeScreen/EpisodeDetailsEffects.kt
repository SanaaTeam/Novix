package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

sealed interface EpisodeDetailsEffects {
    data class NavigateToActorDetails(val actorId: Int) : EpisodeDetailsEffects
    data class PlayTrailer(val trailerUrl: String?) : EpisodeDetailsEffects
    data class ShowErrorSnackBar(val errorMessage: String) : EpisodeDetailsEffects
}