package com.sanaa.presentation.ui_state

data class PeopleScreenUiState(
    val isLoading: Boolean = false,
    val people: List<ActorUiState> = emptyList()
)
