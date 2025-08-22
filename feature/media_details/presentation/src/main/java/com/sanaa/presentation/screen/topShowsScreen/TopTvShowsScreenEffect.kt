package com.sanaa.presentation.screen.topShowsScreen


sealed interface TopTvShowsScreenEffect{
object NavigateBack : TopTvShowsScreenEffect
data class NavigateToTvShowDetails(val id: Int) : TopTvShowsScreenEffect
}