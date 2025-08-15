package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

data class SaveToListUiState(
    val mediaId:Long? = null,
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListsIds: MutableList<Long> = mutableListOf(),
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)

data class PlaylistUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int,
    val itemsIds: List<Long>,
    var containsMediaItem:Boolean = false
)