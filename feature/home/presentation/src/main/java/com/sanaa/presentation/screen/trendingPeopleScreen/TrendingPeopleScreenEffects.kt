package com.sanaa.presentation.screen.trendingPeopleScreen

sealed interface TrendingPeopleScreenEffects {
    object NavigateBack : TrendingPeopleScreenEffects
    data class NavigateToActorDetails(val actorId: Int) : TrendingPeopleScreenEffects
}