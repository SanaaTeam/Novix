package com.sanaa.presentation.screen.trendingPeopleScreen

sealed interface TrendingPeopleScreenEffect {
    object NavigateBack : TrendingPeopleScreenEffect
    data class NavigateToActorDetails(val actorId: Int) : TrendingPeopleScreenEffect
}