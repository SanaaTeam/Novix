package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.screen.playlist.SnackData

data class AddBookmarkListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)