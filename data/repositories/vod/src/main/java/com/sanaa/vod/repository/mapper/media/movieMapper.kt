package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.movie.MovieDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import kotlin.time.Duration.Companion.minutes

fun MovieDto.toEntity(): Movie {
    return Movie(
        id = id,
        posterImageUrl = getFullImageUrl(posterImagePath),
        title = title.orEmpty(),
        genres = genres?.map { it.toEntity() } ?: emptyList(),
        imdbRating = voteAverage ?: -1f,
        duration = duration?.minutes ?: (-1).minutes,
        releaseDate = getLocalDateOrDefault(releaseDate),
        overview = overview.toString(),
        rating = rating?.toInt() ?: -1,
        trailerUrl = "",
    )
}


