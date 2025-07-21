package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import entity.Movie
import kotlinx.datetime.LocalDate

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = getFullImageUrl(posterImagePath),
        title = title.orEmpty(),
        genres = genres?.mapNotNull { it.id?.toGenre() } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0f,
        duration = duration ?: 0,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview)
}

fun List<MovieVideoDto>.toDomain(): String? =
    this.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
        ?.let { "https://www.youtube.com/watch?v=${it.key}" }



