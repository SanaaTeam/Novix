package com.sanaa.presentation.screen.trendingMoviesScreen

sealed interface TrendingMoviesScreenEffect {
    object NavigateBack : TrendingMoviesScreenEffect
    data class NavigateToMoviesDetails(val id: Int) : TrendingMoviesScreenEffect
    object NavigateToLogin: TrendingMoviesScreenEffect
}