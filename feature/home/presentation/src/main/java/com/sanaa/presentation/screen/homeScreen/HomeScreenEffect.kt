package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaType


sealed class HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaType: MediaType): HomeScreenEffect()
    object  NavigateToMoviesScreen: HomeScreenEffect()
    object  NavigateToTvShowsScreen: HomeScreenEffect()
    object  NavigateToPeopleScreen: HomeScreenEffect()
    object  NavigateToTopRatingMediaScreen: HomeScreenEffect()
    object  NavigateToWatchedMediaScreen: HomeScreenEffect()
}