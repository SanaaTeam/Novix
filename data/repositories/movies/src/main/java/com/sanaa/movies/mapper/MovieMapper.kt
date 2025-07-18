package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MoviesByCategoryResponse
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import entity.Movie
import kotlinx.datetime.LocalDate

const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun String?.fullImageUrlOrEmpty(): String =
    if (this.isNullOrBlank()) "" else "$TMDB_IMAGE_BASE_URL$this"

fun MovieDetailsDto.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = posterImagePath.fullImageUrlOrEmpty(),
        title = title.orEmpty(),
        genres = genres.mapNotNull { it.id?.toGenre() },
        imdbRating = voteAverage,
        duration = duration,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview
    )
}

fun SimilarMoviesDto.Results.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = posterPath.fullImageUrlOrEmpty(),
        title = title.orEmpty(),
        genres = genreIds.mapNotNull { it.toGenre() },
        imdbRating = voteAverage?.toFloat(),
        duration = 0,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview
    )

}

fun MoviesByCategoryResponse.MoviesByCategoryDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        posterImageUrl = posterPath.fullImageUrlOrEmpty(),
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        genres = genreIds.mapNotNull { it.toGenre() },
        overview = overview,
        duration = 0,
        imdbRating = voteAverage?.toFloat(),
    )
}

