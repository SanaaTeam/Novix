package com.sanaa.presentation.screen.genreTvShows

import androidx.paging.PagingData
import com.sanaa.presentation.model.SeriesUiModel

data class GenreTvShowsScreenUiState(
    val title: String? = null,
    val tvShows: PagingData<SeriesUiModel> = PagingData.empty(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)



