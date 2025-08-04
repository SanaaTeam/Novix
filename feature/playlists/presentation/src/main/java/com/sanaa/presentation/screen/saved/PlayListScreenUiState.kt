package com.sanaa.presentation.screen.saved


data class PlayListScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val isListAdded: Boolean = false,
    val showAddBottomSheet: Boolean = false,
)

data class SnackData(
    val message: String,
    val isError: Boolean
)