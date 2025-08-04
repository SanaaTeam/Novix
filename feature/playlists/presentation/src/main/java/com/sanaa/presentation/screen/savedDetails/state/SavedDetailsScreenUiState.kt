package com.sanaa.presentation.screen.savedDetails.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SavedDetailsScreenUiState(
    val selectedMediaTypeUi: MediaTypeUi = MediaTypeUi.ALL,
    val movieList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val tvShowList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val allList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)