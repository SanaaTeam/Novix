package com.sanaa.presentation.screen.trendingMediaScreen

sealed class TrendingMediaScreenEffect {
    object NavigateBack : TrendingMediaScreenEffect()
    data class NavigateToMediaDetails(val id: Int) : TrendingMediaScreenEffect()
}