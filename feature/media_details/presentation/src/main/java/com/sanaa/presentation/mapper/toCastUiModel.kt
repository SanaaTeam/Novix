package com.sanaa.presentation.mapper

import com.sanaa.presentation.model.CastMemberUiModel
import entity.Actor

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun Actor.toCastUiModel(): CastMemberUiModel {
    val fullImageUrl = imageUrl.fullPosterUrlOrEmpty()
    return CastMemberUiModel(
        name = name,
        character = character.orEmpty().ifBlank { "Unknown" },
        imageUrl = fullImageUrl
    )
}
fun String?.fullPosterUrlOrEmpty(): String {
    return if (!isNullOrBlank()) "$TMDB_IMAGE_BASE_URL$this" else ""
}