package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.mapper.actor.getFullImageUrl
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



