package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Actor

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    region = region,
    lastShow = lastShow,
    gender = if (gender == Actor.Gender.MALE) "male" else "female",
    department = department,
    character = character,
    lifeSpan = when {
        birthDate != defaultDate && deathDate != defaultDate -> "$birthDate - $deathDate"
        birthDate != defaultDate -> birthDate.toString()
        else -> null
    },
    placeOfBirth = placeOfBirth,
    biography = biography?.takeIf(String::isNotBlank),
)