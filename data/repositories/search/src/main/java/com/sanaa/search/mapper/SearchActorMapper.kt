package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import search.usecase.search_param.SearchActorOutput

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun ActorLocalDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name,
        profileImageUrl = getFullImageUrl(imagePath),
    )
}

fun ActorSearchDto.toLocalDto(language: String): ActorLocalDto {
    return ActorLocalDto(
        id = id,
        name = name ?: "",
        imagePath = getFullImageUrl(profileImagePath),
        language = language,
    )
}

fun ActorSearchDto.toSearchOutput(): SearchActorOutput {
    return SearchActorOutput(
        id = id,
        name = name ?: "",
        profileImageUrl = getFullImageUrl(profileImagePath),
    )
}

internal fun getFullImageUrl(path: String?): String =
    if (path.isNullOrBlank()) "" else "${TMDB_IMAGE_BASE_URL}$path"