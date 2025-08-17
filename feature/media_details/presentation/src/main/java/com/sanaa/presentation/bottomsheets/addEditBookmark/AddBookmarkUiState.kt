package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.screen.movieDetails.SnackData

data class AddBookmarkUiState(
    val listTitle: String = "",
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)