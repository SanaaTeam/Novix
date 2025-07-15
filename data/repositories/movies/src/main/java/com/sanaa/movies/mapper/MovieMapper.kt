package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
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
        title = title ?: "Unknown Title",
        genres = genres.mapNotNull { it.id?.toGenre() },
        imdbRating = voteAverage ?: 0.0f,
        duration = duration ?: 0,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview ?: "No overview available"
    )
}

fun SimilarMoviesDto.Results.toDomain(): Movie {
    return Movie(
        id = id ?: 0,
        posterImageUrl = posterPath.fullImageUrlOrEmpty(),
        title = title ?: "Unknown Title",
        genres = genreIds.mapNotNull { it.toGenre() },
        imdbRating = voteAverage?.toFloat()?: 0.0f ,
        duration = 0,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview ?: "No overview available"
    )

}