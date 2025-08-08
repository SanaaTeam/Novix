package com.sanaa.presentation.screen.watchingHistory

import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi

data class WatchingHistoryUiState(
    val selectedMediaTypeUi: MediaTypeUi = MediaTypeUi.MOVIE,
    val movieList: List<MediaItemUiModel> = emptyList(),
    val tvShowList: List<MediaItemUiModel> = emptyList(),
    val movieGenres: List<GenreState> = emptyList(),
    val tvShowGenres: List<GenreState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val tvShowSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)