package com.sanaa.series.mapper

import com.sanaa.series.dto.ActorDto
import entity.Actor
import entity.Actor.Gender

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun ActorDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        character = character,
        imageUrl = getFullImageUrl(profilePath),
        gender = apiGenderMapping(gender),
        region = null,
        lastShow = null,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        biography = "",
        department = null
    )
}

fun apiGenderMapping(id: Int): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}

internal fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "${TMDB_IMAGE_BASE_URL}$path"