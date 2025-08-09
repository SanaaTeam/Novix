package com.sanaa.presentation.bottomsheets.addEditBookmark

data class AddBookmarksListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)