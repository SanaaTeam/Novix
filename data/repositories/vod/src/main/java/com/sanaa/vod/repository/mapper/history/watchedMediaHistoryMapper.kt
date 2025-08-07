package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import entity.Genre
import entity.MediaHistoryItem
import usecase.search.search_param.MediaType

fun MediaHistoryItem.toDto(username: String): WatchedMediaHistoryLocalDto {
    val genresString =
        this.genres.map { it.id }.joinToString(separator = ",", prefix = ",", postfix = ",")
    return WatchedMediaHistoryLocalDto(
        id = this.id,
        username = username,
        posterImageUrl = this.posterImageUrl,
        mediaType = this.mediaType.name,
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
        id = this.id,
        posterImageUrl = this.posterImageUrl,
        mediaType = MediaType.valueOf(this.mediaType),
        genres = genreList,
        lastWatchedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(this.timestamp)
    )
}