package com.sanaa.presentation.screen.episode_details

import com.sanaa.presentation.screen.series.CastUiModel
import com.sanaa.presentation.screen.series.EpisodeUiModel

data class EpisodeDetailsScreenUiState(
    val isLoading: Boolean = false,
    val episode: EpisodeUiModel = EpisodeUiModel(),
    val guestOfHonor: List<CastUiModel> = emptyList(),
    val imagesUrl: List<String> = emptyList(),
    val error: String? = null,
    val seriesId: Int = 0,
    val trailerUrl: String? = null,
    val showSaveBottomSheet: Boolean = false,
)