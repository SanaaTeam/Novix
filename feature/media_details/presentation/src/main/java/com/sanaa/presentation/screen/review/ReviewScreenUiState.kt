package com.sanaa.presentation.screen.review

import java.time.LocalDate


data class ReviewUiState(
    val id: Int,
    val authorName: String ,
    val username: String? ,
    val content: String ,
    val rating: Float?,
    val createdDate: LocalDate,
    val avatarUrl: String?,
    val error: String? = null,
    val isLoading: Boolean = false,
    val noInternetConnection: Boolean = false
)
