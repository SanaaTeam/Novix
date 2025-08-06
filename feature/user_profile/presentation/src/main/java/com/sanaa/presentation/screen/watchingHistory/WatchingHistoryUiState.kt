package com.sanaa.presentation.screen.watchingHistory

import androidx.paging.PagingData
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class WatchingHistoryUiState(
    val watchingHistory: Flow<PagingData<MediaItemUiModel>> = flowOf(PagingData.Companion.empty()),
    val selectedMediaTypeUi: MediaTypeUi? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)