package com.sanaa.tvapp.presentation.screens.home

import com.sanaa.tvapp.state.MediaTypeUi

sealed class HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi): HomeScreenEffect()
    object  NavigateToMoviesScreen: HomeScreenEffect()
    object  NavigateToTvShowsScreen: HomeScreenEffect()
    object  NavigateToPeopleScreen: HomeScreenEffect()
    object  NavigateToTopRatingMediaScreen: HomeScreenEffect()
    object  NavigateToWatchedMediaScreen: HomeScreenEffect()
    object  NavigateToPlayListScreen: HomeScreenEffect()
}