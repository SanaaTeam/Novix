package com.sanaa.presentation.screen.series

import com.sanaa.presentation.module.ActorUiModel
import com.sanaa.presentation.module.SeasonUiModel
import com.sanaa.presentation.module.SeriesUiModel

data class SeriesScreenUiState(
    val isLoading: Boolean = false,
    val series: SeriesUiModel = SeriesUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<ActorUiModel> = emptyList(),
    val images: List<String> = emptyList(),
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val selectedSeason: Int = 1,
)


