package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.util.DateTimeUtils
import entity.Genre
import entity.MediaHistoryItem
import usecase.search.search_param.MediaType

fun MediaHistoryItem.toDto(
    username: String,
    time: Long = DateTimeUtils.getCurrentTimeStamp()
): WatchedMediaHistoryLocalDto {
    val genresString =
        genres.map { it.id }.joinToString(separator = ",", prefix = ",", postfix = ",")
    return WatchedMediaHistoryLocalDto(
        id = id,
        username = username,
        posterImageUrl = posterImageUrl,
        mediaType = mediaType.name,
        genres = genresString,
        timestamp = time
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
        id = id,
        posterImageUrl = posterImageUrl,
        mediaType = MediaType.valueOf(mediaType),
        genres = genreList,
        lastWatchedAt = timestamp
    )
}