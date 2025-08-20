package com.sanaa.presentation.model

data class ReviewUiModel(
    val id: String,
    val authorName: String? = null,
    val username: String? = null,
    val content: String,
    val rating: String? = null,
    val createdDate: String,
    val avatarUrl: String? = null,
)