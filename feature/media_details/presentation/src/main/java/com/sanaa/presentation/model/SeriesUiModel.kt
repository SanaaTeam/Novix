package com.sanaa.presentation.model

import android.annotation.SuppressLint
import com.sanaa.presentation.util.formatDateLocalizedDigits
import com.sanaa.presentation.util.formatLocalizedDate
import entity.Episode
import entity.Season
import entity.TvSeries

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
    val rating: String? = null,
    val airDate: String? = null,
    val stillPath: String? = null,
    val duration: Int? = null,
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
    releaseDate = releaseDate.formatDateLocalizedDigits().toString(),
)

fun Season.toSeasonUiModel() = SeasonUiModel(
    seasonNumber = number,
    episodeCount = episodes.size,
    episodes = episodes.map { it.toEpisodeUiModel() },

    )

@SuppressLint("DefaultLocale")
fun Episode.toEpisodeUiModel() = EpisodeUiModel(
    number = number,
    title = title,
    rating = String.format("%.1f", imdbRating),
    airDate = releaseDate?.let { formatLocalizedDate(it) },
    stillPath = stillImagePath,
    duration = durationMinutes,
    overview = overview,
    seasonNumber = seasonNumber,
)
