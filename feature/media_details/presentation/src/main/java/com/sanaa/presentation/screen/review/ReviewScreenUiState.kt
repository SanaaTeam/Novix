package com.sanaa.presentation.screen.review

import com.sanaa.presentation.model.ReviewUiModel


data class ReviewScreenUiState(
    val isLoading: Boolean = false,
    val reviews: List<ReviewUiModel> = emptyList(),
    val error: String? = null,
    val noInternetConnection: Boolean = false
)

