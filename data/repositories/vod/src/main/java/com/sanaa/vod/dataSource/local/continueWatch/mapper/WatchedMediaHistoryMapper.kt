package com.sanaa.vod.dataSource.local.continueWatch.mapper

import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto
import entity.Genre
import entity.MediaHistoryItem
import usecase.search.search_param.MediaType

fun MediaHistoryItem.toDto(): WatchedMediaHistoryLocalDto {
    val genresString = this.genres.map { it.id }.joinToString(separator = ",", prefix = ",", postfix = ",")
    return WatchedMediaHistoryLocalDto(
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        mediaType = this.mediaType.name,
        genres = genresString
    )
}

fun WatchedMediaHistoryLocalDto.toEntity(): MediaHistoryItem {
    val genreList = this.genres
        .removePrefix(",")
        .removeSuffix(",")
        .split(",")
        .filter { it.isNotBlank() }
        .mapNotNull { it.toIntOrNull() }
        .map { genreId -> Genre(id = genreId, name = "") }

    return MediaHistoryItem(
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        mediaType = MediaType.valueOf(this.mediaType),
        genres = genreList
    )
}