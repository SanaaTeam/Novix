package com.sanaa.presentation.screen.state

data class ActorUiModel(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val region: String?,
    val lastShow: String?,
    val gender: String,
    val department: String?,
    val character: String?,
    val birthDate: String?,
    val deathDate: String?,
    val placeOfBirth: String?,
    val biography: String,
    val topMovies: List<MovieUiModel> = emptyList(),
    val topTvSeries: List<MovieUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val rating: String,
)