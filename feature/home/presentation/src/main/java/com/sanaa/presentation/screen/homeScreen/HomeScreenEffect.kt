package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaTypeUi


sealed interface HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) :
        HomeScreenEffect

    object NavigateToPlayListScreen : HomeScreenEffect
    object NavigateToMoviesScreen : HomeScreenEffect
    object NavigateToTvShowsScreen : HomeScreenEffect
    object NavigateToPeopleScreen : HomeScreenEffect
    object NavigateToTopRatingMediaScreen : HomeScreenEffect
    object NavigateToWatchedMediaScreen : HomeScreenEffect
    data class ShowError(val message: String) : HomeScreenEffect
    data class ShowSuccess(val message: String) : HomeScreenEffect
}
