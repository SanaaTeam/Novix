package com.sanaa.vod.repository.mapper.cachedContent

import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Genre
import entity.Movie
import entity.TvSeries

fun Movie.toLocalDto(): MovieLocalDto =
    MovieLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl,
        imdbRating = imdbRating ?: 0f,
        releaseDate = releaseDate.toString(),
    )

fun TvSeries.toLocalDto(): TvShowLocalDto =
    TvShowLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl.orEmpty(),
        imdbRating = imdbRating,
        releaseDate = releaseDate.toString(),

        )

fun MovieLocalDto.toEntity() = Movie(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    duration = null,
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = "",
    trailerUrl = "",
    rating = 0,
)

fun TvShowLocalDto.toEntity() = TvSeries(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = "",
    seasonsCount = 0,
    rating = 0,
)

fun GenreLocalDto.toEntity() = Genre(
    id = id,
    name = name,
)

fun Genre.toLocalDto() = GenreLocalDto(
    id = id,
    name = name,
)
