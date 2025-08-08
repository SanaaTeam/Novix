package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

data class SaveToListsUiState(
    val playlists: List<PlaylistUiItems> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)

data class PlaylistUiItems(
    val id: Long,
    val title: String,
    val itemCount: Int
)