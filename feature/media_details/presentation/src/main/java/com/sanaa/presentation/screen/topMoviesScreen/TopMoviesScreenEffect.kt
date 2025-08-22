package com.sanaa.presentation.screen.topMoviesScreen


sealed interface TopMoviesScreenEffect{
object NavigateBack : TopMoviesScreenEffect
data class NavigateToMovieDetails(val id: Int) : TopMoviesScreenEffect
object NavigateToLogin : TopMoviesScreenEffect
}