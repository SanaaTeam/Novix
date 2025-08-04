package com.sanaa.vod.repository.mapper.cachedContent

import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun Movie.toCachedContentLocalDto(): CachedContentLocalDto =
    CachedContentLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl,
        imdbRating = imdbRating ?: 0f,
        mediaType = CachedContentLocalDto.MediaType.MOVIE.name,
        releaseDate = releaseDate.toString(),
        genres = "",
        metadataId = 0,
    )

fun TvSeries.toCachedContentLocalDto(): CachedContentLocalDto =
    CachedContentLocalDto(
        id = id,
        title = title,
        posterImageUrl = posterImageUrl.orEmpty(),
        imdbRating = imdbRating,
        mediaType = CachedContentLocalDto.MediaType.TV_SHOW.name,
        releaseDate = releaseDate.toString(),
        genres = "",
        metadataId = 0,
    )

fun CachedContentLocalDto.toMovie() = Movie(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    duration = null,
    releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1, 1, 1),
    overview = "",
    trailerUrl = "",
    rating = 0,
)

fun CachedContentLocalDto.toTvSeries() = TvSeries(
    id = id,
    title = title,
    posterImageUrl = posterImageUrl,
    imdbRating = imdbRating,
    genres = emptyList(),
    releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1, 1, 1),
    overview = "",
    seasonsCount = 0,
    rating = 0,
)

fun CachedContentLocalDto.toDomain(): Any? =
    when (mediaType) {
        CachedContentLocalDto.MediaType.MOVIE.name -> toMovie()
        CachedContentLocalDto.MediaType.TV_SHOW.name -> toTvSeries()
        else -> null
    }
