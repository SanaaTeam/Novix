package com.sanaa.movies.mapper

import com.sanaa.movies.dataSource.dto.ActorDto
import entity.Actor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun ActorDto.toDomain(): Actor{
    return Actor(
        id = id,
        name = name ?: "Unknown",
        imageUrl = profileImagePath ?: "",
        gender = gender,
        character = character,
        biography = biography,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        region = null,
        lastShow = null
    )
}


fun Actor.toDto(): ActorDto{
    return ActorDto(
        id = id,
        name = name,
        profileImagePath = imageUrl,
        gender = gender,
        character = character,
        biography = biography
    )
}