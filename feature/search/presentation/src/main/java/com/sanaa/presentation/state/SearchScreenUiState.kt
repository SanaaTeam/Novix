package com.sanaa.presentation.state

data class SearchScreenUiState(
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val movies: List<MovieUiModel> = emptyList(),
    val tvShows: List<TvShowUiModel> = emptyList(),
    val actors: List<ActorUiModel> = emptyList(),
    val error: String? = null
)

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageRes: Int,
    val rating: String
)

data class ActorUiModel(
    val id: Int,
    val name: String,
    val imageRes: Int
)

data class TvShowUiModel(
    val id: Int,
    val title: String,
    val imageRes: Int,
    val rating: String
)

