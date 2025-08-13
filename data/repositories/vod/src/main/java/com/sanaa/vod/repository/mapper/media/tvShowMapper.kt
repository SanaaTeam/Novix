package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.TvShow

fun TvShowDto.toEntity(): TvShow {
    return TvShow(
        id = id,
        title = name,
        overview = overview.orEmpty(),
        posterImageUrl = getFullImageUrl(posterPath),
        imdbRating = voteAverage,
        releaseDate = getLocalDateOrDefault(firstAirDate),
        genres = genres.map { it.toEntity() },
        seasonsCount = seasonsCount,
        rating = rating?.toInt() ?: -1
    )
}