package com.sanaa.presentation.bottomsheets.addBookmark

data class AddEditBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
    val isEditMode: Boolean = false
)