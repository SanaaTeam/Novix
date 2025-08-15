package com.sanaa.presentation.model

data class TvShowUiState(
    val id: Int = 0,
    val title: String = "",
    val posterPath: String? = null,
    val rating: String = "",
    val overview: String = "",
    val genres: List<GenreUiModel> = emptyList(),
    val seasonsCount: Int = 0,
    val trailerUrl: String? = null,
    val releaseDate: String = "",
)

data class SeasonUiState(
    val seasonNumber: Int = 0,
    val episodeCount: Int = 0,
    val episodes: List<EpisodeUiState> = emptyList(),
)

data class EpisodeUiState(
    val number: Int = 0,
    val title: String = "",
    val rating: String? = null,
    val airDate: String? = null,
    val stillPath: String? = null,
    val duration: Int? = null,
    val overview: String? = null,
    val seasonNumber: Int = 0,
)
