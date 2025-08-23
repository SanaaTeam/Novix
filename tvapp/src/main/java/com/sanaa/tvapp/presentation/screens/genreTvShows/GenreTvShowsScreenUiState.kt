package com.sanaa.tvapp.presentation.screens.genreTvShows

import androidx.paging.PagingData
import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class GenreTvShowsScreenUiState(
    val title: String? = null,
    val categoryId: Int = 0,
    val tvShows: Flow<PagingData<TvShowUiModel>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val snackBarData: SnackData? = null,
)

enum class ScreenState {
    LOADING,
    NO_INTERNET,
    CONTENT
}