package com.sanaa.presentation.screen.actor

sealed class ActorScreenEffects {
    object NavigateBack : ActorScreenEffects()
    data class NavigateToTopMovies(val actorId: Int) : ActorScreenEffects()
    data class NavigateToTopSeries(val actorId: Int) : ActorScreenEffects()
    data class NavigateToGallery(val actorId: Int) : ActorScreenEffects()
}
