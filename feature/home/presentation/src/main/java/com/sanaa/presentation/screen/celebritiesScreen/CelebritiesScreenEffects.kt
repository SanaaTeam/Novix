package com.sanaa.presentation.screen.celebritiesScreen

sealed interface CelebritiesScreenEffects {
    object NavigateBack : CelebritiesScreenEffects
    data class NavigateToActorDetails(val actorId: Int) : CelebritiesScreenEffects
}