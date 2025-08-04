package com.sanaa.presentation.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class WatchingHistoryUiState(
    val watchingHistory: Flow<PagingData<MediaItem>>,
    val selectedMediaTypeUi: MediaTypeUi? = null, 
    val isLoading: Boolean = false,
    val error: String? = null
) 