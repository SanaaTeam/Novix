package com.sanaa.presentation.screen.playlist

import usecase.custom_list.custom_list_param.SavedList


data class PlayListScreenUiState(
    val screenState: PlaylistScreenState = PlaylistScreenState.Loading,
    val isLoading: Boolean = true,
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val isListAdded: Boolean = false,
    val showAddBottomSheet: Boolean = false,
    val lists: List<PlayListUiModel> = emptyList(),
    val snackData: SnackData? = null
)

data class SnackData(
    val message: String,
    val isError: Boolean
)

data class PlayListUiModel(
    val id: Int = 0,
    val title: String = "",
    val mediaCount: Int = 1
)

fun SavedList.toUiModel() = PlayListUiModel(
    id = id,
    title = title,
    mediaCount = itemCount
)

enum class PlaylistScreenState {
    Loading,
    NoInternet,
    Guest,
    Empty,
    WithItems
}