package com.sanaa.presentation.screen.review

import androidx.paging.PagingData
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.screen.movieDetails.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class ReviewScreenUiState(
    val isLoading: Boolean = false,
    val reviews: Flow<PagingData<ReviewUiModel>> = flowOf(PagingData.empty()),
    val noInternetConnection: Boolean = false,
    val snackBarData: SnackData? = null
)

