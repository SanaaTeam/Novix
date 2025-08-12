package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

sealed class EpisodeDetailsEffects {
    data class NavigateToActorDetails(val actorId: Int) : EpisodeDetailsEffects()
    data class PlayTrailer(val trailerUrl: String?) : EpisodeDetailsEffects()
    data object  ShowErrorSnackBar : EpisodeDetailsEffects()
    data object ShowSuccessSnackBar : EpisodeDetailsEffects()
    object NavigateToLogin : EpisodeDetailsEffects()
}