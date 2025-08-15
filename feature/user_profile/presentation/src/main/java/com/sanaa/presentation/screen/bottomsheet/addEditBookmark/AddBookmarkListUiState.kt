package com.sanaa.presentation.screen.bottomsheet.addEditBookmark

import com.sanaa.presentation.screen.bottomsheet.components.SnackData


data class AddBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)