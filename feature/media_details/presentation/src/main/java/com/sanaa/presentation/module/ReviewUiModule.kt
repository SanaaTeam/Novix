package com.sanaa.presentation.module

import entity.Review
import kotlin.math.roundToInt

data class ReviewUiModule(
    val id: Int,
    val authorName: String? = null,
    val username: String? = null,
    val content: String,
    val rating: String? = null,
    val createdDate: String,
    val avatarUrl: String? = null,
)

fun Review.toReviewUiModule() = ReviewUiModule(
    id = id,
    authorName = authorName?.toString(),
    username = userHandle,
    content = content,
    rating =  rating?.roundToInt()?.toString(),
    createdDate = createdDate.toString(),
    avatarUrl = avatarUrl
)