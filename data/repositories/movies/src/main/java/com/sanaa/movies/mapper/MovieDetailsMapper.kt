package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.dto.MovieDetailsDto
import entity.Movie
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

fun MovieDetailsDto.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = posterImagePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        title = title ?: "Unknown Title",
        genres = genreIds?.mapNotNull { it.toGenre() } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0f,
        duration = duration ?: 0,
        releaseDate = releaseDate?.toLocalDate() ?: LocalDate(1970, 1, 1),
        overview = overview ?: "No overview available"
    )
}
