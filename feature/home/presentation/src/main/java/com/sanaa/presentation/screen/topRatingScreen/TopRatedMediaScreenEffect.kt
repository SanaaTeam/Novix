package com.sanaa.presentation.screen.topRatingScreen

import com.sanaa.presentation.state.MediaTypeUiState

sealed interface TopRatedMediaScreenEffect{
    object NavigateBack : TopRatedMediaScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUiState) : TopRatedMediaScreenEffect
    object NavigateToLogin : TopRatedMediaScreenEffect
}