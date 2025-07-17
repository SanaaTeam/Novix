package com.sanaa.actors.mapper

import com.sanaa.actors.dataSource.remote.dto.ActorDto
import com.sanaa.actors.dataSource.remote.dto.ActorMovieCastDto.MovieCastCreditDto
import com.sanaa.actors.dataSource.remote.dto.ActorTvCastDto.TvCastCreditDto
import entity.Actor
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun String?.fullImageUrlOrEmpty(): String =
    if (this.isNullOrBlank()) "" else "$TMDB_IMAGE_BASE_URL$this"

fun ActorDto.toDomain(): Actor = Actor(
    id = id,
    imageUrl = profileImagePath.fullImageUrlOrEmpty(),
    name = name ?: "Unknown",
    region = null,
    department = knownForDepartment,
    lastShow = null,
    gender = when (gender) {
        1 -> Actor.Gender.FEMALE
        2 -> Actor.Gender.MALE
        else -> Actor.Gender.MALE
    },
    character = null,
    birthDate = birthDay?.takeIf(String::isNotBlank)?.let(LocalDate::parse),
    deathDate = deathDay?.takeIf(String::isNotBlank)?.let(LocalDate::parse),
    placeOfBirth = placeOfBirth,
    biography = biography ?: ""
)

fun MovieCastCreditDto.toDomain(): Movie = Movie(
    id = movieId,
    posterImageUrl = posterPath.fullImageUrlOrEmpty(),
    title = title ?: originalTitle ?: "Unknown",
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: 0f,
    duration = 0,
    releaseDate = releaseDate.toLocalDateOrNull() ?: LocalDate(1900, 1, 1),
    overview = overview ?: ""
)

fun TvCastCreditDto.toDomain(): TvSeries = TvSeries(
    id = tvId,
    title = name ?: originalName ?: "Unknown",
    overview = overview ?: "",
    releaseDate = firstAirDate.toLocalDateOrNull() ?: LocalDate(1900, 1, 1),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: 0f,
    posterImageUrl = posterPath.fullImageUrlOrEmpty(),
    seasonsCount = 0
)

private fun String?.toLocalDateOrNull(): LocalDate? =
    this
        ?.takeIf(String::isNotBlank)
        ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
