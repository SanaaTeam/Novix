package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import usecase.search.SearchActorOutput

fun ActorsLocalDto.toOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name,
        profileImageUrl = imagePath ?: "",
    )
}

fun List<ActorsLocalDto>.toOutput(): List<SearchActorOutput> {
    return map { it.toOutput() }
}

fun ActorSearchDto.toLocalDto(language: String): ActorsLocalDto {
    return ActorsLocalDto(
        id = id,
        name = name ?: "",
        imagePath = profileImagePath ?: "",
        language = language,
    )
}

fun ActorSearchDto.toOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name ?: "",
        profileImageUrl = profileImagePath ?: "",
    )
}

fun List<ActorSearchDto>.toOutput(): List<SearchActorOutput> {
    return map { it.toOutput() }
}