package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.ReviewUiModel
import com.sanaa.presentation.util.formatDateLocalizedDigits
import entity.Review


@SuppressLint("DefaultLocale")
fun Review.toReviewUiModel() = ReviewUiModel(
    id = id,
    authorName = authorName?.toString(),
    username = userHandle,
    content = content,
    rating = rating?.let { String.format("%.1f", rating) },
    createdDate = createdDate.formatDateLocalizedDigits().toString(),
    avatarUrl = avatarUrl
)