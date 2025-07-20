package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MoviesByCategoryResponse
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import entity.Movie
import kotlinx.datetime.LocalDate

fun MovieDetailsDto.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = getFullImageUrl(posterImagePath) ,
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
        id = id,
        posterImageUrl = getFullImageUrl(posterPath),
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
        title = title ?: "Unknown Title",
        posterImageUrl = getFullImageUrl(posterPath),
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        genres = genreIds.mapNotNull { it.toGenre() },
        overview = overview ?: "",
        duration = 0,
        imdbRating = voteAverage?.toFloat() ?: 0.0f,
    )
}


