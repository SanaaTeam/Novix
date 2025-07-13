package com.sanaa.presentation.state

import usecase.search.MediaFilters


data class SearchScreenUiState(
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val recentViewedMedia: List<RecentViewedUiModel> = emptyList(),
    val recentSearchQueries: List<RecentSearchUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val movies: List<MovieUiModel> = emptyList(),
    val tvShows: List<TvShowUiModel> = emptyList(),
    val actors: List<ActorUiModel> = emptyList(),
    val error: String? = null,
    val filters: MediaFilters? = null,
)

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
    val mediaType: String = "",
    val isSaved: Boolean = false,
)

enum class MediaTypeUi {
    MOVIE,
    TV_SERIES,
}
