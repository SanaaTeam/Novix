package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.EpisodeUiModel
import com.sanaa.presentation.model.SeasonUiModel
import com.sanaa.presentation.model.SeriesUiModel
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import com.sanaa.presentation.util.DateTimeUtils.formatLocalizedDate
import entity.Episode
import entity.MediaHistoryItem
import entity.Season
import entity.TvShow
import kotlinx.datetime.Clock
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun TvShow.toSeriesUiModel(trailerUrl: String? = null) = SeriesUiModel(
    id = id,
    title = title,
    posterPath = posterImageUrl,
    overview = overview,
    rating = String.format("%.1f", imdbRating),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
    genres = genres.map { it.toUiModel() },
    releaseDate = if (releaseDate != defaultDate) releaseDate.toString() else "",
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
    airDate = if (releaseDate != defaultDate) formatLocalizedDate(releaseDate) else null,
    stillPath = stillImagePath,
    duration = durationMinutes,
    overview = overview,
    seasonNumber = seasonNumber,
)

fun TvShow.toHistory(): MediaHistoryItem {
    return MediaHistoryItem(
        id = id,
        genres = genres,
        posterImageUrl = posterImageUrl.orEmpty(),
        mediaType = MediaType.TV_SERIES,
        lastWatchedAt = Clock.System.now()
    )
}