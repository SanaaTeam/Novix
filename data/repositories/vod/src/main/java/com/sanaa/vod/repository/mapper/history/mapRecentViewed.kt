package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.util.TimeUtils
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.search_param.MediaType

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