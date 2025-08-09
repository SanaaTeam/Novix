package com.sanaa.presentation.screen.playlist

import usecase.custom_list.custom_list_param.SavedList


data class PlayListScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val isListAdded: Boolean = false,
    val showAddBottomSheet: Boolean = false,
    val lists: List<PlayListUiModel> = emptyList()
)

data class SnackData(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val isError: Boolean
)

data class PlayListUiModel(
    val id: Int = 0,
    val title: String = "",
    val mediaCount: Int = 1
)

fun PlayListUiModel.toDomain() = SavedList(
    id = id,
    title = title,
    itemCount = mediaCount
)

fun SavedList.toUiModel() = PlayListUiModel(
    id = id,
    title = title,
    mediaCount = itemCount
)