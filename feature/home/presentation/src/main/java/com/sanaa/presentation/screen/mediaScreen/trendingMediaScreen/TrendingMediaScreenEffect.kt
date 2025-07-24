package com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen

sealed class TrendingMediaScreenEffect {
    object NavigateBack : TrendingMediaScreenEffect()
    data class NavigateToTrendingMediaDetails(val id: Int) : TrendingMediaScreenEffect()
}