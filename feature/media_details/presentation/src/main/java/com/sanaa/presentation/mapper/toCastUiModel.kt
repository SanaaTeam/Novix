package com.sanaa.presentation.mapper

import com.sanaa.presentation.model.CastMemberUiModel
import entity.Actor

fun Actor.toCastUiModel(): CastMemberUiModel {
    return CastMemberUiModel(
        name = name,
        character = character ?: "Unknown",
        imageUrl = imageUrl
    )
}