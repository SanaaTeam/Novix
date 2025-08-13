package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Review

@SuppressLint("DefaultLocale")
fun Review.toReviewUiModel() = ReviewUiModel(
    id = id,
    authorName = authorName,
    username = userHandle,
    content = content,
    rating = rating.takeIf { it > 0 }?.let { String.format("%.1f", rating) },
    createdDate = if (createdDate != defaultDate) createdDate.toString() else "",
    avatarUrl = avatarUrl
)