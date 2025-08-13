package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface TopRatedScreenEffect{
    object NavigateBack : TopRatedScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : TopRatedScreenEffect
    object NavigateToLogin : TopRatedScreenEffect
}