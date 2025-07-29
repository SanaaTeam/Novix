package com.sanaa.presentation.screen.review

import androidx.paging.PagingData
import com.sanaa.presentation.model.ReviewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class ReviewScreenUiState(
    val isLoading: Boolean = false,
    val reviews: Flow<PagingData<ReviewUiModel>> = flowOf(PagingData.empty()),
    val error: String? = null,
)

