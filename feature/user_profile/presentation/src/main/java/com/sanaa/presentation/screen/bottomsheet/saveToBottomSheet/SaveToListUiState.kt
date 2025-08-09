package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

data class SaveToListUiState(
    val playlists: List<PlaylistUiItem> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)

data class PlaylistUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int
)