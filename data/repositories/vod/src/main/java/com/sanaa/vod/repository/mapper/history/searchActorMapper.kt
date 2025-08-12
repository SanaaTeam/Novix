package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Actor

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
        birthDate = getLocalDateOrDefault(null),
        deathDate = getLocalDateOrDefault(null),
        placeOfBirth = null,
        biography = null,
    )
}

fun mapGender(gender: Int?): Actor.Gender {
    return if (gender == 1) Actor.Gender.FEMALE else Actor.Gender.MALE
}