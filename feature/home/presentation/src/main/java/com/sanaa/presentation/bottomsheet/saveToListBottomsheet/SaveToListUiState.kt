package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import com.sanaa.presentation.components.SnackData

data class SaveToListUiState(
    val mediaId: Int? = null,
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListsIds: MutableList<Int> = mutableListOf(),
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiItem(
    val id: Int,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Int>,
    var containsMediaItem: Boolean = false
)