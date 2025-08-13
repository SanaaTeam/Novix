package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import entity.TvShow

fun MovieSearchDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage,
        duration = null,
        releaseDate = getLocalDateOrDefault(releaseDate),
        overview = "",
        trailerUrl = null,
        rating = null
    )
}

fun TvShowSearchDto.toEntity(): TvShow {
    return TvShow(
        id = id,
        title = name.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0f,
        overview = "",
        releaseDate = getLocalDateOrDefault(releaseDate),
        seasonsCount = 0,
        rating = null
    )
}