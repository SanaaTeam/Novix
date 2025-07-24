package com.sanaa.presentation.model

import entity.Genre

data class GenreUiState(
    val id: Int,
    val name: String,
)

fun GenreUiState.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun Genre.toState(): GenreUiState {
    return GenreUiState(
        id = id,
        name = name
    )
}
fun List<Genre>.toState(): List<GenreUiState> {
    return map { it.toState() }
}
fun List<GenreUiState>.toDomain(): List<Genre> {
    return map { it.toDomain() }
}