package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun MovieSearchDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage,
        duration = null,
        releaseDate = parseReleaseDate(releaseDate),
        overview = "",
        trailerUrl = null,
        rating = null
    )
}

fun TvShowSearchDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = name.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0f,
        overview = "",
        releaseDate = parseReleaseDate(releaseDate),
        seasonsCount = 0,
        rating = null
    )
}


fun parseReleaseDate(dateString: String?): LocalDate {
    return if (!dateString.isNullOrBlank()) {
        try {
            LocalDate.parse(dateString)
        } catch (e: IllegalArgumentException) {
            LocalDate(1970, 1, 1)
        }
    } else {
        LocalDate(1970, 1, 1)
    }
}