package com.sanaa.vod.dataSource.local.continueWatch.mapper

import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto
import com.sanaa.vod.util.TimeUtils
import entity.Genre
import entity.MediaHistoryItem
import usecase.search.search_param.MediaType

fun MediaHistoryItem.toDto(username: String, time: Long = TimeUtils.getCurrentTimeStamp()): WatchedMediaHistoryLocalDto {
    val genresString =
        this.genres.map { it.id }.joinToString(separator = ",", prefix = ",", postfix = ",")
    return WatchedMediaHistoryLocalDto(
        id = this.id,
        username = username,
        posterImageUrl = this.posterImageUrl,
        mediaType = this.mediaType.name,
        genres = genresString,
        isSaved = this.isSaved,
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
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        mediaType = MediaType.valueOf(this.mediaType),
        genres = genreList,
        isSaved = this.isSaved,
        lastWatchedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(this.timestamp)
    )
}