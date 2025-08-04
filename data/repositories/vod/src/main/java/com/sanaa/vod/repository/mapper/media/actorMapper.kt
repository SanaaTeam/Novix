package com.sanaa.vod.repository.mapper.media


import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import entity.Actor
import entity.Actor.Gender
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun ActorDto.toDomain(): Actor = Actor(
    id = id,
    imageUrl = getFullImageUrl(profileImagePath),
    name = name ?: "Unknown name",
    region = null,
    department = knownForDepartment,
    lastShow = null,
    gender = apiGenderMapping(gender),
    character = null,
    birthDate = birthDay?.takeIf(String::isNotBlank)?.let(LocalDate::parse),
    deathDate = deathDay?.takeIf(String::isNotBlank)?.let(LocalDate::parse),
    placeOfBirth = placeOfBirth,
    biography = biography ?: ""
)


fun ActorCastCreditDto.toMovie(): Movie = Movie(
    id = id,
    posterImageUrl = getFullImageUrl(posterPath),
    title = movieTitle.orEmpty(),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: 0f,
    duration = null,
    releaseDate = toLocalDateOrNull(releaseDate ?: firstAirDate) ?: LocalDate(1900, 1, 1),
    overview = overview ?: "",
    rating = null
)

fun ActorCastCreditDto.toTvSeries(): TvSeries = TvSeries(
    id = id,
    title = tvShowTitle.orEmpty(),
    overview = overview.orEmpty(),
    releaseDate = toLocalDateOrNull(firstAirDate ?: releaseDate) ?: LocalDate(1900, 1, 1),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: 0f,
    posterImageUrl = getFullImageUrl(posterPath),
    seasonsCount = 0,
    rating = null
)


fun apiGenderMapping(id: Int?): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}

internal fun toLocalDateOrNull(value: String?): LocalDate? =
    value
        ?.takeIf { it.isNotBlank() }
        ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }