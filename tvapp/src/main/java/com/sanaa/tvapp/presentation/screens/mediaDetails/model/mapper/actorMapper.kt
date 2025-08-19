package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import entity.Actor

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    department = department,
    character = character,
    lifeSpan = when {
        birthDate != null && deathDate != null -> "$birthDate - $deathDate"
        birthDate != null -> birthDate!!.toString()
        else -> null
    },
    placeOfBirth = placeOfBirth?.toString(),
    biography = biography?.takeIf(String::isNotBlank),
)