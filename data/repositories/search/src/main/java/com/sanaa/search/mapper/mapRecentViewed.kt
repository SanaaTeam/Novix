package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import search.usecase.search_param.MediaType
import search.usecase.search_param.RecentViewedMedia

fun RecentViewedMedia.toDto(time: Long = System.currentTimeMillis()): RecentViewedLocalDto {
    return RecentViewedLocalDto(
        id = id,
        imageUrl = posterImageUrl,
        isSaved = isSaved,
        mediaType = mediaType.name,
        timestamp = time
    )
}

fun RecentViewedLocalDto.toEntity(): RecentViewedMedia {
    return RecentViewedMedia(
        id = id,
        posterImageUrl = imageUrl,
        isSaved = isSaved,
        mediaType = MediaType.valueOf(mediaType)
    )
}