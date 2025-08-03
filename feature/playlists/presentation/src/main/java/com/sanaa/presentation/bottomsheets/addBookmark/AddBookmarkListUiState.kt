package com.sanaa.presentation.bottomsheets.addBookmark

data class AddBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false
)