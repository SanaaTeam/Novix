package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaTypeUiState


sealed interface HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUiState) :
        HomeScreenEffect

    object NavigateToPlayListScreen : HomeScreenEffect
    object NavigateToTrendingMoviesScreen : HomeScreenEffect
    object NavigateToTrendingTvShowsScreen : HomeScreenEffect
    object NavigateToTrendingPeopleScreen : HomeScreenEffect
    object NavigateToTopRatingMediaScreen : HomeScreenEffect
    object NavigateToWatchedMediaScreen : HomeScreenEffect
    object NavigateToLogin : HomeScreenEffect
}
