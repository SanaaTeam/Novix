package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.screen.movieDetails.SnackData

data class SaveToListBottomSheetUiState(
    val mediaId:Long? = null,
    val playlists: List<PlaylistUiStateItem> = emptyList(),
    val selectedListsIds: MutableList<Long> = mutableListOf(),
    val isLoading: Boolean = false,
    val isUploading:Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val snackBarData: SnackData? = null
)

data class PlaylistUiStateItem(
    val id: Long,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Long>,
    var containsMediaItem:Boolean = false
)