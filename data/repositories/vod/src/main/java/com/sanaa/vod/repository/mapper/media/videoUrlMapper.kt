package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.VideoDto


fun List<VideoDto>.toEntity(): String? =
    this.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
        ?.let {
            "https://www.youtube.com/watch?v=${it.key}"
        }