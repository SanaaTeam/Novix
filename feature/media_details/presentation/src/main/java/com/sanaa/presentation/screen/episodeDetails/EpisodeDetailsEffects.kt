package com.sanaa.presentation.screen.episodeDetails

sealed class EpisodeDetailsEffects {
    data class NavigateToActorDetails(val actorId: Int) : EpisodeDetailsEffects()
    object NavigateBack : EpisodeDetailsEffects()
    data class PlayTrailer(val trailerUrl: String?) : EpisodeDetailsEffects()
    object NavigateToLogin : EpisodeDetailsEffects()
}