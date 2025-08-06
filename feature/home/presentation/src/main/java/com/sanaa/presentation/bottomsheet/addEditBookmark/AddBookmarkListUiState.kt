package com.sanaa.presentation.bottomsheet.addEditBookmark

data class AddBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)