package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import entity.Review
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonNull.content

fun ReviewDto.toEntity(): Review {
    return Review(
        id = id.toInt(),
        content = content,
        authorName = authorDetails.name,
        userHandle = authorDetails.username,
        avatarUrl = buildAvatarUrl(authorDetails.avatarPath),
        rating = authorDetails.rating,
        createdDate = LocalDate.parse(createdAt.substring(0, 10))
    )
}

fun buildAvatarUrl(avatarPath: String?): String? {
    if (avatarPath.isNullOrBlank()) return null
    return when {
        avatarPath.startsWith("/https") || avatarPath.startsWith("/http") ->
            avatarPath.removePrefix("/")

        avatarPath.startsWith("/") ->
            "https://image.tmdb.org/t/p/w185$avatarPath"

        else -> avatarPath
    }
}