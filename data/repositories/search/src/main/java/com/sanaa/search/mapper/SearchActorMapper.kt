package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import usecase.search.SearchActorOutput

fun ActorsLocalDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name,
        profileImageUrl = imageUrl + imagePath ?: "",
    )
}

fun ActorSearchDto.toLocalDto(language: String): ActorsLocalDto {
    return ActorsLocalDto(
        id = id,
        name = name ?: "",
        imagePath = imageUrl + profileImagePath ?: "",
        language = language,
    )
}

fun ActorSearchDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name ?: "",
        profileImageUrl = imageUrl + profileImagePath ?: "",
    )
}
