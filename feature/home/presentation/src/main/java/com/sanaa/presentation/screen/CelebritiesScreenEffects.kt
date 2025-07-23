package com.sanaa.presentation.screen

sealed interface CelebritiesScreenEffects {
    object NavigateBack : CelebritiesScreenEffects
    data class NavigateToActorDetails(val actorId: Int) : CelebritiesScreenEffects
}