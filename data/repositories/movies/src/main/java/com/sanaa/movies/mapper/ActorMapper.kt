package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.remote.dto.ActorDto
import entity.Actor
import entity.Actor.Gender

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun ActorDto.toDomain(): Actor {
    return Actor(
        id = id,
        name = name,
        imageUrl = getFullImageUrl(profilePath),
        gender = apiGenderMapping(gender),
        character = character,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        region = null,
        lastShow = null,
        department = null,
        biography = null
    )
}


fun apiGenderMapping(id: Int?): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}

internal fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "${TMDB_IMAGE_BASE_URL}$path"