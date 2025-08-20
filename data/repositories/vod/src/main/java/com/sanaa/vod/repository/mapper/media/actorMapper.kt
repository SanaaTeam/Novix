package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Actor
import entity.Movie
import entity.TvShow
import kotlin.time.Duration.Companion.minutes

fun ActorDto.toEntity(): Actor = Actor(
    id = id,
    imageUrl = getFullImageUrl(profileImagePath),
    name = name.orEmpty(),
    department = knownForDepartment.orEmpty(),
    character = "",
    birthDate = getLocalDateOrDefault(birthDay),
    deathDate = getLocalDateOrDefault(deathDay),
    placeOfBirth = placeOfBirth.orEmpty(),
    biography = biography.orEmpty()
)

fun ActorCastCreditDto.toMovie(): Movie = Movie(
    id = id,
    posterImageUrl = getFullImageUrl(posterPath),
    title = movieTitle.orEmpty(),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: -1f,
    duration = (-1).minutes,
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = overview.orEmpty(),
    rating = -1,
    trailerUrl = ""
)

fun ActorCastCreditDto.toTvShow(): TvShow = TvShow(
    id = id,
    title = tvShowTitle.orEmpty(),
    overview = overview.orEmpty(),
    releaseDate = getLocalDateOrDefault(firstAirDate),
    genres = emptyList(),
    imdbRating = voteAverage?.toFloat() ?: -1f,
    posterImageUrl = getFullImageUrl(posterPath),
    seasonsCount = 0,
    rating = -1
)