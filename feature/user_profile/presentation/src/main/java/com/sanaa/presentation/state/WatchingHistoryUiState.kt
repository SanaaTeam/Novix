package com.sanaa.presentation.state

import androidx.paging.PagingData
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.flow.Flow

data class WatchingHistoryUiState(
    val watchingHistory: Flow<PagingData<MediaItem>>,
    val selectedMediaTypeUi: MediaTypeUi = MediaTypeUi.MOVIE,
    val isLoading: Boolean = false,
    val error: String? = null
) 