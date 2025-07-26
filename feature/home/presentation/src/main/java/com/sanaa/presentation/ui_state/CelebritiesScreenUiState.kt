package com.sanaa.presentation.ui_state

data class CelebritiesScreenUiState(
    val isLoading: Boolean = false,
    val celebrities: List<PersonUiState> = emptyList(),
    val isNoInternetConnection: Boolean = false
)
