package com.sanaa.presentation.model

import android.annotation.SuppressLint
import com.sanaa.presentation.util.formatDateLocalizedDigits
import entity.Review

data class ReviewUiModel(
    val id: Int,
    val authorName: String? = null,
    val username: String? = null,
    val content: String,
    val rating: String? = null,
    val createdDate: String,
    val avatarUrl: String? = null,
)

@SuppressLint("DefaultLocale")
fun Review.toReviewUiModel() = ReviewUiModel(
    id = id,
    authorName = authorName?.toString(),
    username = userHandle,
    content = content,
    rating = String.format("%.1f", rating),
    createdDate = createdDate.formatDateLocalizedDigits().toString(),
    avatarUrl = avatarUrl
)