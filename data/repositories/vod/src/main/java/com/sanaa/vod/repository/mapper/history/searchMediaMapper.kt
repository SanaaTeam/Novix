package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import entity.TvShow
import kotlin.time.Duration.Companion.minutes

fun MovieSearchDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage ?: -1f,
        duration = (-1).minutes,
        releaseDate = getLocalDateOrDefault(releaseDate),
        overview = overview.orEmpty(),
        trailerUrl = "",
        rating = -1
    )
}

fun TvShowSearchDto.toEntity(): TvShow {
    return TvShow(
        id = id,
        title = name.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage ?: -1f,
        overview = "",
        releaseDate = getLocalDateOrDefault(releaseDate),
        seasonsCount = 0,
        rating = -1
    )
}