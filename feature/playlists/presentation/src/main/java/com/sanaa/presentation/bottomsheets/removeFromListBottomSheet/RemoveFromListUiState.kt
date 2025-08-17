package com.sanaa.presentation.bottomsheets.removeFromListBottomSheet

import com.sanaa.presentation.screen.playlist.SnackData

data class RemoveFromListUiState(
    val mediaId:Int? = null,
    val mediaTitle: String = "",
    val playlists: List<PlaylistUiItem> = emptyList(),
    val deselectedListsIds: MutableList<Int> = mutableListOf(),
    val isLoading: Boolean = false,
    val isUploading:Boolean = false,
    val isRemoveButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiItem(
    val id: Int,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Int>,
)