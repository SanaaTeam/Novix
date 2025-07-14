package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.dto.ActorDto
import entity.Actor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import entity.Actor.Gender
fun ActorDto.toDomain(): Actor{
    return Actor(
        id = id,
        name = name ?: "Unknown",
        imageUrl = profileImagePath ?: "",
        gender = apiGenderMapping(gender),
        character = character,
        biography = biography,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        region = null,
        lastShow = null
    )
}
fun apiGenderMapping(id: Int): Gender {
    return when (id) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
        else -> Gender.MALE
    }
}