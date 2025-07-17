package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import search.usecase.search_param.SearchActorOutput

fun ActorLocalDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name,
        profileImageUrl = (imageUrl + imagePath ),
    )
}

fun ActorSearchDto.toLocalDto(language: String): ActorLocalDto {
    return ActorLocalDto(
        id = id,
        name = name ?: "",
        imagePath = (imageUrl + profileImagePath),
        language = language,
    )
}

fun ActorSearchDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name ?: "",
        profileImageUrl = (imageUrl + profileImagePath),
    )
}

