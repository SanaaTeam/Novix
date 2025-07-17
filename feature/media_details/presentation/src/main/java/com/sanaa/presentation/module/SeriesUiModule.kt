package com.sanaa.presentation.module

import android.annotation.SuppressLint
import entity.Episode
import entity.Season
import entity.TvSeries
import kotlin.math.roundToInt

data class SeriesUiModel(
    val id: Int = 0,
    val title: String = "",
    val posterPath: String? = null,
    val rating: String = "",
    val overview: String? = null,
    val genres: List<String> = emptyList(),
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
    val rating: String = "",
    val airDate: String = "",
    val stillPath: String? = null,
    val duration: Int = 0,
    val overview: String? = null,
    val seasonNumber: Int = 0,
)

@SuppressLint("DefaultLocale")
fun TvSeries.toSeriesUiModel(trailerUrl: String? = null) = SeriesUiModel(
    id = id,
    title = title,
    posterPath = posterImageUrl,
    overview = overview,
    rating = String.format("%.1f", imdbRating),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
    genres = genres.map { it.name },
    releaseDate = releaseDate.toString(),
)

fun Season.toSeasonUiModel() = SeasonUiModel(
    seasonNumber = number,
    episodeCount = episodes.size,
    episodes = episodes.map { it.toEpisodeUiModel() },

    )

fun Episode.toEpisodeUiModel() = EpisodeUiModel(
    number = number,
    title = title,
    rating = imdbRating.roundToInt().toString(),
    airDate = "3 oct 2011",
    stillPath = stillImagePath,
    duration = durationMinutes,
    overview = overview,
    seasonNumber = seasonNumber,
)
