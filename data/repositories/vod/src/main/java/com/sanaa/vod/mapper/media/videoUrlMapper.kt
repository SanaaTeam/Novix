package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.VideoDto


fun List<VideoDto>.toDomain(): String? =
    this.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
        ?.let { "https://www.youtube.com/watch?v=${it.key}" }
