package com.sanaa.presentation.state

import androidx.paging.PagingData
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.flow.Flow

data class WatchingHistoryUiState(
    val watchingHistory: Flow<PagingData<MediaItem>> = kotlinx.coroutines.flow.flowOf(PagingData.empty()),
    val selectedMediaTypeUi: MediaTypeUi? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 