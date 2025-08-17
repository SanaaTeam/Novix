package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.screen.componants.SnackData

data class SaveToListUiState(
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int
)