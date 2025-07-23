package com.sanaa.presentation.ui_state

sealed interface PeopleScreenEffects {
    object NavigateBack : PeopleScreenEffects
    data class NavigateToActorDetails(val actorId: Int) : PeopleScreenEffects
}
