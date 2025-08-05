package com.sanaa.presentation.screen.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchScreenUiState(
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val recentViewedMedia: List<RecentViewedUiModel> = emptyList(),
    val recentSearchQueries: List<RecentSearchUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val noInternetConnection: Boolean = false,
    val movies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val tvShows: Flow<PagingData<TvShowUiModel>> = flowOf(PagingData.empty()),
    val actors: Flow<PagingData<ActorUiModel>> = flowOf(PagingData.empty()),
    val error: String? = null,
    val lastTabIndex: Int = -1,
    val showLoginBottomSheet: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val isDarkMode: Boolean = false,
    val safeContentThreshold: Float = 0f,
) {

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
    val rating: String,
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
    val rating: String,
)

data class RecentSearchUiModel(
    val id: Int = 0,
    val title: String = "",
)

data class RecentViewedUiModel(
    val id: Int = 0,
    val imageUrl: String = "",
    val mediaType: MediaTypeUi = MediaTypeUi.MOVIE,
    val isSaved: Boolean = false,
)

enum class MediaTypeUi {
    MOVIE,
    TV_SERIES,
}