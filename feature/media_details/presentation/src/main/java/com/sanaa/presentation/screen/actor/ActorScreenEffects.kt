package com.sanaa.presentation.screen.actor

sealed class ActorScreenEffects {
    object NavigateBack : ActorScreenEffects()
    data class NavigateToTopMovies(val actorId: Int) : ActorScreenEffects()
    data class NavigateToTopTvShows(val actorId: Int) : ActorScreenEffects()
    data class NavigateToGallery(val actorId: Int) : ActorScreenEffects()
    data class NavigateToMovieDetails(val movieId: Int) : ActorScreenEffects()
    data class NavigateToTvShowDetails(val tvShowId: Int) : ActorScreenEffects()
    object NavigateToLogin : ActorScreenEffects()
}
