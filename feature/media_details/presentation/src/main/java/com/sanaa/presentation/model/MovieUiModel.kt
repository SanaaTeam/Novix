package com.sanaa.presentation.model

import kotlin.time.Duration

data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val rating: String? = null,
    val releaseDate: String = "",
    val duration: Duration? = null,
    val posterUrls: List<String> = emptyList(),
    val genres: List<GenreUiModel> = emptyList(),
    val isBookmarked: Boolean = false,
    val trailerUrl: String? = null,
    val posterUrl: String? = null
)
