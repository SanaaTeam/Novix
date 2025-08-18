package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.screen.movieDetails.SnackData

data class SaveToListBottomSheetUiState(
    val mediaId:Int? = null,
    val playlists: List<PlaylistUiStateItem> = emptyList(),
    val selectedListsIds: MutableList<Int> = mutableListOf(),
    val isLoading: Boolean = false,
    val isUploading:Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiStateItem(
    val id: Int,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Int>,
    var containsMediaItem:Boolean = false
)