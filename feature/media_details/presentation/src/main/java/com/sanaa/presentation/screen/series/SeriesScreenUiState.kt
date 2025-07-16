package com.sanaa.presentation.screen.series

import entity.Actor
import entity.Episode
import entity.Season
import entity.TvSeries

data class SeriesScreenUiState(
    val isLoading: Boolean = false,
    val series: SeriesUiModel = SeriesUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<CastUiModel> = emptyList(),
    val images: List<String> = emptyList(),
    val error: String? = null,
    val selectedSeason: Int = 1,
)

data class SeriesUiModel(
    val id: Int = 0,
    val title: String = "",
    val posterPath: String? = null,
    val rating: String = "",
    val overview: String = "",
    val genres: List<String> = emptyList(),
    val seasonsCount: Int = 0,
    val trailerUrl: String? = null,
)

data class SeasonUiModel(
    val seasonNumber: Int = 0,
    val episodeCount: Int = 0,
    val episodes: List<EpisodeUiModel> = emptyList(),
)

data class EpisodeUiModel(
    val episodeNumber: Int = 0,
    val title: String = "",
    val rating: String = "",
    val airDate: String = "",
    val stillPath: String? = null,
    val duration: Int = 0,
)

data class CastUiModel(
    val id: Int = 0,
    val name: String = "",
    val character: String = "",
    val profilePath: String? = null,
)

fun TvSeries.toSeriesUiModel(trailerUrl: String?) = SeriesUiModel(
    id = id,
    title = title,
    posterPath = posterImageUrl,
    overview = overview,
    rating = imdbRating.toString(),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
)

fun Season.toSeasonUiModel() = SeasonUiModel(
    seasonNumber = number,
    episodeCount = episodes.size,
    episodes = episodes.map { it.toEpisodeUiModel() },

    )

fun Episode.toEpisodeUiModel() = EpisodeUiModel(
    episodeNumber = number,
    title = title,
    rating = imdbRating.toInt().toString(),
    airDate = "3 oct 2011",
    stillPath = stillImagePath,
    duration = durationMinutes,
)

fun Actor.toCastUiModel() = CastUiModel(
    id = id,
    name = name,
    character = character.orEmpty(),
    profilePath = imageUrl,
)