package com.sanaa.tvapp.presentation.screens.mediaDetails.model

import com.sanaa.presentation.model.GenreUiModel
import kotlin.time.Duration

data class MovieDetailsUiModel(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val rating: String? = null,
    val releaseDate: String = "",
    val duration: Duration? = null,
    val posterUrls: List<String> = emptyList(),
    val genres: List<GenreUiModel> = emptyList(),
    val trailerUrl: String? = null,
    val posterUrl: String? = null
)

