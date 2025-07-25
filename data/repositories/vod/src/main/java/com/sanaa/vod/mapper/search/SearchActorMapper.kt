package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import entity.Actor

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun ActorLocalDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        imageUrl = getFullImageUrl(imagePath),
        region = null,
        lastShow = null,
        gender = mapGender(gender),
        department = null,
        character = null,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        biography = null,
    )
}

fun ActorSearchDto.toLocalDto(language: String): ActorLocalDto {
    return ActorLocalDto(
        id = id,
        name = name ?: "",
        imagePath = getFullImageUrl(profileImagePath),
        language = language,
        gender = gender
    )
}

fun ActorSearchDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name.orEmpty(),
        imageUrl = getFullImageUrl(profileImagePath),
        region = null,
        lastShow = null,
        gender = mapGender(gender),
        department = knownForDepartment,
        character = null,
        birthDate = null,
        deathDate = null,
        placeOfBirth = null,
        biography = null,
    )
}

internal fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "${TMDB_IMAGE_BASE_URL}$path"

fun mapGender(gender: Int?): Actor.Gender {
    return if (gender == 1) Actor.Gender.FEMALE else Actor.Gender.MALE
}