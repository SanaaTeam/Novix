package com.sanaa.vod.repository.mapper.media


import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Actor
import entity.Actor.Gender
import entity.Movie
import entity.TvSeries

fun ActorDto.toEntity(): Actor = Actor(
    id = id,
    imageUrl = getFullImageUrl(profileImagePath),
    name = name ?: "Unknown name",
    region = null,
    department = knownForDepartment,
    lastShow = null,
    gender = apiGenderMapping(gender),
    character = null,
    birthDate = getLocalDateOrDefault(birthDay),
    deathDate = getLocalDateOrDefault(deathDay),
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
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = overview ?: "",
    rating = null
)

fun ActorCastCreditDto.toTvSeries(): TvSeries = TvSeries(
    id = id,
    title = tvShowTitle.orEmpty(),
    overview = overview.orEmpty(),
    releaseDate = getLocalDateOrDefault(firstAirDate),
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