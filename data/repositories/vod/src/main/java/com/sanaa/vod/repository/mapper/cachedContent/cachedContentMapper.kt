package com.sanaa.vod.repository.mapper.cachedContent

import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Genre
import entity.Movie
import entity.TvShow
import kotlin.time.Duration.Companion.minutes

fun Movie.toLocalDto(): MovieLocalDto =
    MovieLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl,
        imdbRating = imdbRating,
        releaseDate = releaseDate.toString(),
        overview = overview,
    )

fun TvShow.toLocalDto(): TvShowLocalDto =
    TvShowLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl,
        imdbRating = imdbRating,
        releaseDate = releaseDate.toString(),

        )

fun MovieLocalDto.toEntity() = Movie(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    duration = (-1).minutes,
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = overview,
    trailerUrl = "",
    rating = -1,
)

fun TvShowLocalDto.toEntity() = TvShow(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = "",
    seasonsCount = 0,
    rating = -1,
)

fun GenreLocalDto.toEntity() = Genre(
    id = id,
    name = name,
)

fun Genre.toLocalDto() = GenreLocalDto(
    id = id,
    name = name,
)
