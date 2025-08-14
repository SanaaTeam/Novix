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
        department = knownForDepartment.orEmpty(),
        character = "",
        birthDate = getLocalDateOrDefault(null),
        deathDate = getLocalDateOrDefault(null),
        placeOfBirth = "",
        biography = "",
    )
}