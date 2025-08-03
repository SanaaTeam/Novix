package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.model.ActorUiModel
import entity.Actor

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    region = region,
    lastShow = lastShow,
    gender = if (gender == Actor.Gender.MALE) "male" else "female",
    department = department?.toString(),
    character = character,
    lifeSpan = when {
        birthDate != null && deathDate != null -> "$birthDate - $deathDate"
        birthDate != null -> birthDate!!.toString()
        else -> null
    },
    placeOfBirth = placeOfBirth?.toString(),
    biography = biography?.takeIf(String::isNotBlank),
)