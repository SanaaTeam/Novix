package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Review

fun ReviewDto.toEntity(): Review {
    return Review(
        id = id,
        content = content,
        authorName = authorDetails.name.orEmpty(),
        userHandle = authorDetails.username.orEmpty(),
        avatarUrl = buildAvatarUrl(authorDetails.avatarPath).orEmpty(),
        rating = authorDetails.rating ?: -1f,
        createdDate = getLocalDateOrDefault(createdAt.substring(0, 10))
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