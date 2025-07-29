package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.mapper.actor.getFullImageUrl
import entity.Movie
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.minutes

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        posterImageUrl = getFullImageUrl(posterImagePath),
        title = title.orEmpty(),
        genres = genres?.map { it.toEntity() } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0f,
        duration = duration?.minutes ,
        releaseDate = releaseDate?.let(LocalDate::parse) ?: LocalDate(1900, 1, 1),
        overview = overview
    )
}



