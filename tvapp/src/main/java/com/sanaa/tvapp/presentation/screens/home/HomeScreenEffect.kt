package com.sanaa.tvapp.presentation.screens.home

import com.sanaa.tvapp.state.MediaTypeUiState

sealed interface HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUiState) :
        HomeScreenEffect
}