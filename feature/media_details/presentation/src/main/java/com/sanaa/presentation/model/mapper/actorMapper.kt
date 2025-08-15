package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Actor

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl.takeIf(String::isNotBlank),
    name = name,
    department = department.takeIf(String::isNotBlank),
    character = character.takeIf(String::isNotBlank),
    lifeSpan = when {
        birthDate != defaultDate && deathDate != defaultDate -> "$birthDate - $deathDate"
        birthDate != defaultDate -> birthDate.toString()
        else -> null
    },
    placeOfBirth = placeOfBirth.takeIf(String::isNotBlank),
    biography = biography.takeIf(String::isNotBlank),
)