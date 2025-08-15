package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.screen.componants.SnackData

data class AddBookmarksListUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null)