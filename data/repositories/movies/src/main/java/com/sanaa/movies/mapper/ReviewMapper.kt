package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import entity.Review
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonNull.content


fun ReviewDto.Results.toDomain(): Review {
    return Review(
        id = id?.toInt() ?: 0,
        authorName = authorDetails?.name ?: "Unknown",
        userHandle = authorDetails?.username,
        avatarUrl = authorDetails?.avatarPath?.let { avatarPath ->
            when {
                avatarPath.startsWith("/https") -> avatarPath.removePrefix("/")
                avatarPath.startsWith("/") -> "https://image.tmdb.org/t/p/w500$avatarPath"
                else -> avatarPath
            }
        },
        rating = authorDetails?.rating,
        content = content ?: "",
        createdDate = createdAt?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1)
    )
}

