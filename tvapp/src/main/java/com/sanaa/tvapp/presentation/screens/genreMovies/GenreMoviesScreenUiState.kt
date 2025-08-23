package com.sanaa.tvapp.presentation.screens.genreMovies

import androidx.paging.PagingData
import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class GenreMoviesScreenUiState(
    val title: String? = null,
    val categoryId: Int = 0,
    val movies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSaveToListBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null,
)

