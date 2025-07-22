package com.sanaa.vod.mapper.actor


import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import entity.Actor
import entity.Actor.Gender
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

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
    duration = 0,
    releaseDate = toLocalDateOrNull(releaseDate ?: firstAirDate) ?: LocalDate(1900, 1, 1),
    overview = overview ?: ""
)

fun ActorCastCreditDto.toTvSeries(): TvSeries = TvSeries(
    id = id,
    title = tvShowTitle.orEmpty(),
    overview = overview.orEmpty(),
    releaseDate = toLocalDateOrNull(firstAirDate ?: releaseDate) ?: LocalDate(1900, 1, 1),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: 0f,
    posterImageUrl = getFullImageUrl(posterPath),
    seasonsCount = 0
)


fun apiGenderMapping(id: Int?): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}

internal fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "$TMDB_IMAGE_BASE_URL$path"

internal fun toLocalDateOrNull(value: String?): LocalDate? =
    value
        ?.takeIf { it.isNotBlank() }
        ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }