package com.sanaa.presentation.model

data class ActorUiModel(
    val id: Int = 0,
    val imageUrl: String? = null,
    val name: String = "",
    val department: String? = null,
    val character: String? = null,
    val lifeSpan: String? = null,
    val placeOfBirth: String? = null,
    val biography: String? = null,
)

