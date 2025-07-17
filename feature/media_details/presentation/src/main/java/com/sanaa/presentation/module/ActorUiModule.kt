package com.sanaa.presentation.module

import entity.Actor

data class ActorUiModel(
    val id: Int = 0,
    val imageUrl: String? = null,
    val name: String = "",
    val region: String? = null,
    val lastShow: String? = null,
    val gender: String = "",
    val department: String? = null,
    val character: String? = null,
    val lifeSpan: String? = null,
    val placeOfBirth: String? = null,
    val biography: String? = null,
)
fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    region = region,
    lastShow = lastShow,
    gender = if (gender == Actor.Gender.MALE) "male" else "female",
    department = department?.toString(),
    character = character,
    lifeSpan = birthDate?.let { birth -> deathDate?.let { death -> "$birth - $death" } ?: birth }
        .toString(),
    placeOfBirth = placeOfBirth?.toString(),
    biography = biography?.takeIf(String::isNotBlank),
)