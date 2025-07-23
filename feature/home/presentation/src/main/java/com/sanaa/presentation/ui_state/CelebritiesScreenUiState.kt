package com.sanaa.presentation.ui_state

data class CelebritiesScreenUiState(
    val isLoading: Boolean = false,
    val people: List<PersonUiState> = emptyList()
)
