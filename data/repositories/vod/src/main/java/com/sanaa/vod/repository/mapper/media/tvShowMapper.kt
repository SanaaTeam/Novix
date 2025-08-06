package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun TvShowDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = name,
        overview = overview.toString(),
        posterImageUrl = getFullImageUrl(posterPath),
        imdbRating = voteAverage,
        releaseDate = firstAirDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        genres = genres.map { it.toEntity() },
        seasonsCount = seasonsCount,
        rating = rating?.toInt()
    )
}