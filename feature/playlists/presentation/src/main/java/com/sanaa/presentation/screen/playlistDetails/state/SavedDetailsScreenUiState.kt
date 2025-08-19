package com.sanaa.presentation.screen.playlistDetails.state

import androidx.paging.PagingData
import com.sanaa.presentation.screen.playlist.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SavedDetailsScreenUiState(
    val movieList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val showListDeletionConfirmationBottomSheet: Boolean = false,
    val title: String? = null,
    val listId: Int? = null,
    val showRemoveFromListBottomSheet: Boolean = false,
    val selectedMediaToRemove: MediaItem? = null,
    val snackBarData: SnackData? = null,
    val noInternetConnection: Boolean = false,
    val safeContentThreshold: Float = 0.5f,
)