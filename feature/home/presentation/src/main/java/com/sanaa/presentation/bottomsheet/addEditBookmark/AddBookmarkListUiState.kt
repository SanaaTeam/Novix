package com.sanaa.presentation.bottomsheet.addEditBookmark

import com.sanaa.presentation.components.SnackData

data class AddBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)