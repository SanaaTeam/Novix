package com.sanaa.presentation.screen.celebritiesScreen

import com.sanaa.presentation.state.PersonUiState

data class CelebritiesScreenUiState(
    val isLoading: Boolean = false,
    val celebrities: List<PersonUiState> = emptyList(),
    val isNoInternetConnection: Boolean = false
)