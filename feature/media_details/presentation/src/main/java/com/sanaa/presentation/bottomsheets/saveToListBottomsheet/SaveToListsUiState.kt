package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.screen.movieDetails.SnackData

data class SaveToListsUiState(
    val playlists: List<PlaylistUiItems> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiItems(
    val id: Long,
    val title: String,
    val itemCount: Int
)