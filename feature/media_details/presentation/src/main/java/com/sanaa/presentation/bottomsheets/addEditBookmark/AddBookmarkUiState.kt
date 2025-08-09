package com.sanaa.presentation.bottomsheets.addEditBookmark

data class AddBookmarkUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)