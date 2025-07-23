package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun TvShowDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = name,
        overview = overview,
        posterImageUrl = buildPosterUrl(posterPath),
        imdbRating = voteAverage,
        releaseDate = LocalDate.parse(firstAirDate),
        genres = genres.map { it.toEntity() },
        seasonsCount = seasonsCount,
    )
}

fun buildPosterUrl(posterPath: String?): String? {
    return posterPath?.takeIf { it.isNotBlank() }?.let { "https://image.tmdb.org/t/p/w500$it" }
}