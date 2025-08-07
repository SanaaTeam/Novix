package com.sanaa.presentation.screen.trendingMediaScreen

sealed interface TrendingMediaScreenEffect {
    object NavigateBack : TrendingMediaScreenEffect
    data class NavigateToMediaDetails(val id: Int) : TrendingMediaScreenEffect
    object NavigateToLogin: TrendingMediaScreenEffect
    data class ShowError(val message: String) : TrendingMediaScreenEffect
}