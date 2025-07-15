package com.sanaa.series.mapper

import com.sanaa.series.dto.ActorDto
import entity.Actor
import entity.Actor.Gender


fun ActorDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        character = character,
        imageUrl = getProfileImageUrl(profilePath),
        gender = apiGenderMapping(gender),
        region = null,
        lastShow = null,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        biography = ""
    )
}

fun apiGenderMapping(id: Int): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}

fun getProfileImageUrl(profilePath: String?): String {
    return profilePath.let {
        "https://image.tmdb.org/t/p/w185$it"
    }
}