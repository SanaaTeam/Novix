package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.ImageDto

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun ImageDto.toEntity(): String {
    return "https://image.tmdb.org/t/p/w500$filePath"
}

fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "$TMDB_IMAGE_BASE_URL$path"
