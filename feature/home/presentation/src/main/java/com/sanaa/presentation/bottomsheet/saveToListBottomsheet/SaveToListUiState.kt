package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import com.sanaa.presentation.components.SnackData

data class SaveToListUiState(
    val mediaId:Long? = null,
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListsIds: MutableList<Long> = mutableListOf(),
    val isLoading: Boolean = false,
    val isUploading:Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Long>,
    var containsMediaItem:Boolean = false
)