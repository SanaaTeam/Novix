package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import entity.Actor

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    department = department,
    character = character,
    lifeSpan = "$birthDate - $deathDate",
    placeOfBirth = placeOfBirth,
    biography = biography.takeIf(String::isNotBlank),
)