package com.sanaa.tvapp.presentation.screens.mediaDetails.model


data class TvShowDetailsUiModel(
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

data class SeasonUiModel(
    val seasonNumber: Int = 0,
    val episodeCount: Int = 0,
    val episodes: List<EpisodeUiModel> = emptyList(),
)

data class EpisodeUiModel(
    val number: Int = 0,
    val title: String = "",
    val rating: String? = null,
    val airDate: String? = null,
    val stillPath: String? = null,
    val duration: Int? = null,
    val overview: String? = null,
    val seasonNumber: Int = 0,
)

data class ActorUiModel(
    val id: Int = 0,
    val imageUrl: String? = null,
    val name: String = "",
    val region: String? = null,
    val lastShow: String? = null,
    val gender: String = "",
    val department: String? = null,
    val character: String? = null,
    val lifeSpan: String? = null,
    val placeOfBirth: String? = null,
    val biography: String? = null,
)
