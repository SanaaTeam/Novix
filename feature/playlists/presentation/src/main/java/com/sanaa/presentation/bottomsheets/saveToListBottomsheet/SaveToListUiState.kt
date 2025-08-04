package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

data class SaveToListUiState(
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false
)

data class PlaylistUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int
)