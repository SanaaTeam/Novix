package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

data class SavetoListUiState(
    val playlists: List<PlayListUiItem> = emptyList(),
    val selectedListId: Long? = null,
    val isLoading: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val errorMessage: String? = null
)

data class PlayListUiItem(
    val id: Long,
    val title: String,
    val itemCount: Int
)