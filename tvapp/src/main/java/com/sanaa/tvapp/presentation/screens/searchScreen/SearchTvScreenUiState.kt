package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class SearchTvScreenUiState(
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val noInternetConnection: Boolean = false,
    val movies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val tvShows: Flow<PagingData<TvShowUiModel>> = flowOf(PagingData.empty()),
    val actors: Flow<PagingData<ActorUiModel>> = flowOf(PagingData.empty()),
    val error: String? = null,
    val lastTabIndex: Int = -1,
    val showLoginBottomSheet: Boolean = false,
    val isUserLoggedIn: Boolean = false
){

    companion object {
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
        const val ACTOR_INDEX = 2
    }
}

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
)

data class ActorUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

data class TvShowUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
)