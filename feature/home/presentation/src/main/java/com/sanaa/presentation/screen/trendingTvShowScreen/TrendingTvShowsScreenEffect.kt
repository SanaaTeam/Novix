package com.sanaa.presentation.screen.trendingTvShowScreen

sealed interface TrendingTvShowsScreenEffect {
    object NavigateBack : TrendingTvShowsScreenEffect
    data class NavigateToTvShowDetails(val id: Int) : TrendingTvShowsScreenEffect
}