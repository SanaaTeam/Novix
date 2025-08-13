package com.sanaa.presentation.screen.topRatingScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface TopRatedMediaScreenEffect{
    object NavigateBack : TopRatedMediaScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : TopRatedMediaScreenEffect
    object NavigateToLogin : TopRatedMediaScreenEffect
}