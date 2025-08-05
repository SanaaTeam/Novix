package com.sanaa.presentation.screen.savedDetails.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SavedDetailsScreenUiState(
    val movieList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val title: String? = null,
)