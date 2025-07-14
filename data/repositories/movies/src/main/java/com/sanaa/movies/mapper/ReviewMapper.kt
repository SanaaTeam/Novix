package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.dto.ReviewDto
import entity.Review
import kotlinx.datetime.LocalDate


fun ReviewDto.toDomain(): Review {
    return Review(
        id = id.toInt(),
        authorName = authorDetails.name ?: author,
        userHandle = authorDetails.username,
        avatarUrl = authorDetails.avatarPath?.let { avatarPath ->
            when {
                avatarPath.startsWith("/https") -> avatarPath.removePrefix("/")
                avatarPath.startsWith("/") -> "https://image.tmdb.org/t/p/w500$avatarPath"
                else -> avatarPath
            }
        },
        rating = authorDetails.rating,
        content = content,
        createdDate = LocalDate.parse(createdAt)
    )
}

