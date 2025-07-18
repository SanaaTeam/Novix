package com.sanaa.presentation.model

import entity.Review
import kotlin.math.roundToInt

data class ReviewUiModel(
    val id: Int,
    val authorName: String? = null,
    val username: String? = null,
    val content: String,
    val rating: String? = null,
    val createdDate: String,
    val avatarUrl: String? = null,
)

fun Review.toReviewUiModel() = ReviewUiModel(
    id = id,
    authorName = authorName?.toString(),
    username = userHandle,
    content = content,
    rating =  rating?.roundToInt()?.toString(),
    createdDate = createdDate.toString(),
    avatarUrl = avatarUrl
)