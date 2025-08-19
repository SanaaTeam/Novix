package com.sanaa.tvapp.presentation.screens.home

import com.sanaa.tvapp.state.MediaTypeUiState

sealed class HomeScreenEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUiState): HomeScreenEffect()
}