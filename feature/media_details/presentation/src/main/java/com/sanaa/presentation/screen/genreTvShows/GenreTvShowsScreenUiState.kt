package com.sanaa.presentation.screen.genreTvShows

import com.sanaa.presentation.model.SeriesUiModel

data class GenreTvShowsScreenUiState(
    val title: String? = null,
    val tvShows: List<SeriesUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false
)



