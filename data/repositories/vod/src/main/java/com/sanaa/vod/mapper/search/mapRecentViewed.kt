package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.search.dto.RecentViewedLocalDto
import com.sanaa.vod.util.TimeUtils
import search.usecase.ManageRecentViewedUseCase.RecentViewedMedia
import search.usecase.search_param.MediaType

fun RecentViewedMedia.toDto(time: Long = TimeUtils.getCurrentTimeStamp()): RecentViewedLocalDto {
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