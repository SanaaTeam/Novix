package com.sanaa.presentation.screen.review

import java.time.LocalDate


data class ReviewScreenUiState(
    val isLoading: Boolean = false,
    val reviews: List<ReviewUiModule> = emptyList(),
    val error: String? = null,
)

data class ReviewUiModule(
    val id: Int,
    val authorName: String,
    val username: String?,
    val content: String,
    val rating: Float?,
    val createdDate: LocalDate,
    val avatarUrl: String?,
)
