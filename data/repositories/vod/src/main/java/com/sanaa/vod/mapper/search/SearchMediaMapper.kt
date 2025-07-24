package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeParseException

fun MovieLocalDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title,
        posterImageUrl = getFullImageUrl(imagePath),
        genres = emptyList(),
        imdbRating = imdbRating,
        duration = null,
        releaseDate = releaseDate?.takeIf { it.isNotBlank() }?.let {
            LocalDate.parse(it)
        } ?: LocalDate(1990, 1, 1),
        overview = null,
        trailerUrl = null,
    )
}

fun MovieSearchDto.toLocalDto(language: String): MovieLocalDto {
    return MovieLocalDto(
        id = id,
        title = title ?: "",
        imagePath = getFullImageUrl(posterImagePath),
        language = language,
        releaseDate = releaseDate,
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun MovieSearchDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage,
        duration = null,
        releaseDate = releaseDate?.takeIf { it.isNotBlank() }?.let {
            LocalDate.parse(it)
        } ?: LocalDate(1990, 1, 1),
        overview = null,
        trailerUrl = null,
    )
}

fun TvSeriesLocalDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = title,
        posterImageUrl = getFullImageUrl(imagePath),
        genres = emptyList(),
        imdbRating = imdbRating ?: 0f,
        overview = null,
        releaseDate = releaseDate?.takeIf { it.isNotBlank() }?.let {
            LocalDate.parse(it)
        } ?: LocalDate(1990, 1, 1),
        seasonsCount = 0,
    )
}

fun TvShowSearchDto.toLocalDto(language: String): TvSeriesLocalDto {
    return TvSeriesLocalDto(
        id = id,
        title = name.orEmpty(),
        imagePath = getFullImageUrl(posterImagePath),
        language = language,
        releaseDate = releaseDate,
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun TvShowSearchDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = name.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0f,
        overview = null,
        releaseDate = releaseDate?.takeIf { it.isNotBlank() }?.let {
            LocalDate.parse(it)
        } ?: LocalDate(1990, 1, 1),
        seasonsCount = 0,
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