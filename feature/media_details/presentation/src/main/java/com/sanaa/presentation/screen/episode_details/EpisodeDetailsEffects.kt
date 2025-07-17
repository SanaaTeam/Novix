package com.sanaa.presentation.screen.episode_details

sealed class EpisodeDetailsEffects {
    data class NavigateToActorDetails(val actorId: Int) : EpisodeDetailsEffects()
    object NavigateBack : EpisodeDetailsEffects()
    data class PlayTrailer(val trailerUrl: String?) : EpisodeDetailsEffects()
}