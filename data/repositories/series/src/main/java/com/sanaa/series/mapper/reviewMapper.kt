package com.sanaa.series.mapper

import com.sanaa.series.dto.ReviewDto
import entity.Review
import kotlinx.datetime.LocalDate

fun ReviewDto.toEntity(): Review{
    return Review(
        id = id,
        content = content,
        authorName =authorDetails.name,
        userHandle = authorDetails.username,
        avatarUrl = authorDetails.avatarPath,
        rating = authorDetails.rating,
        createdDate = LocalDate.parse(createdAt)
    )
}
