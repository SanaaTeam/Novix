package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.module.ActorUiModel
import com.sanaa.presentation.module.MovieUiModel
import com.sanaa.presentation.module.SeriesUiModel

data class ActorScreenUiState(
    val actor: ActorUiModel = ActorUiModel(),
    val topMovies: List<MovieUiModel> = emptyList(),
    val topTvSeries: List<SeriesUiModel> = emptyList(),
    val profileImages: List<String> = emptyList(),
    val galleryImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)


