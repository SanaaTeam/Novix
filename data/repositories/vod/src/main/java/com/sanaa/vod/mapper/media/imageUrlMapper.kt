package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.ImageDto

fun ImageDto.toEntity(): String {
    return "https://image.tmdb.org/t/p/w500$filePath"
}