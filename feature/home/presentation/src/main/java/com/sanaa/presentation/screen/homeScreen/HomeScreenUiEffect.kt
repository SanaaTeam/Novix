package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.model.MediaType

sealed class HomeScreenUiEffect {
    data class NavigateToMediaDetails(val mediaId:Int,val mediaType: MediaType): HomeScreenUiEffect()
    object  NavigateToMoviesScreen: HomeScreenUiEffect()
    object  NavigateToTvSowsScreen: HomeScreenUiEffect()
    object  NavigateToPeopleScreen: HomeScreenUiEffect()
    object  NavigateToTopRatingMediaScreen: HomeScreenUiEffect()
    object  NavigateToWatchedMediaScreen: HomeScreenUiEffect()
}