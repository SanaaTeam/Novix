package com.sanaa.presentation.screen.series

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.SeasonUiModel
import com.sanaa.presentation.model.SeriesUiModel

data class SeriesScreenUiState(
    val isLoading: Boolean = false,
    val series: SeriesUiModel = SeriesUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<ActorUiModel> = emptyList(),
    val images: List<String> = emptyList(),
    val showLoginBottomSheet: Boolean = false,
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val selectedSeason: Int = 1,
    val noInternetConnection: Boolean = false
)


