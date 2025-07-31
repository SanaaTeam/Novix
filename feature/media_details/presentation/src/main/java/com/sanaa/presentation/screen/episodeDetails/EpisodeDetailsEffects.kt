package com.sanaa.presentation.screen.episodeDetails

import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect

sealed class EpisodeDetailsEffects {
    data class NavigateToActorDetails(val actorId: Int) : EpisodeDetailsEffects()
    object NavigateBack : EpisodeDetailsEffects()
    data class PlayTrailer(val trailerUrl: String?) : EpisodeDetailsEffects()
    data object  ShowErrorSnackBar : EpisodeDetailsEffects()
    data object ShowSuccessSnackBar : EpisodeDetailsEffects()
    object NavigateToLogin : EpisodeDetailsEffects()
}