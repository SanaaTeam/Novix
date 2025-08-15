package com.sanaa.presentation.bottomsheets.deletebottomsheet

import com.sanaa.presentation.screen.playlist.SnackData

data class DeleteListUiState(
    val isLoading: Boolean = false,
    val snackBarData: SnackData? = null
)