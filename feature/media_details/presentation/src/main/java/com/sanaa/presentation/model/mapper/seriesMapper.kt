package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.EpisodeUiModel
import com.sanaa.presentation.model.SeasonUiModel
import com.sanaa.presentation.model.SeriesUiModel
import com.sanaa.presentation.util.formatDateLocalizedDigits
import com.sanaa.presentation.util.formatLocalizedDate
import entity.Episode
import entity.MediaHistoryItem
import entity.Season
import entity.TvSeries
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun TvSeries.toSeriesUiModel(trailerUrl: String? = null) = SeriesUiModel(
    id = id,
    title = title,
    posterPath = posterImageUrl,
    overview = overview.toString(),
    rating = String.format("%.1f", imdbRating),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
    genres = genres.map { it.toUiModel() },
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

fun TvSeries.toHistory(): MediaHistoryItem {
    return MediaHistoryItem(
        id = id,
        genres = genres,
        posterImageUrl = posterImageUrl.orEmpty(),
        mediaType = MediaType.TV_SERIES
    )
}