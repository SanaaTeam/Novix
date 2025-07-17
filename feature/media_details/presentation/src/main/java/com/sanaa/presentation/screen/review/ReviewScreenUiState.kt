package com.sanaa.presentation.screen.review

import com.sanaa.presentation.module.ReviewUiModule


data class ReviewScreenUiState(
    val isLoading: Boolean = false,
    val reviews: List<ReviewUiModule> = emptyList(),
    val error: String? = null,
)

