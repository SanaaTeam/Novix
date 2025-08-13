package com.sanaa.presentation.state

import entity.Actor

data class PersonUiState(
    val id: Int,
    val name: String,
    val character: String? = null,
    val imageUrl: String
)

fun Actor.toState() = PersonUiState(
    id = id,
    name = name,
    character = character,
    imageUrl = imageUrl
)