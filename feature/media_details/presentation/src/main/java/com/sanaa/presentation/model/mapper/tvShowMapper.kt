package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.EpisodeUiState
import com.sanaa.presentation.model.SeasonUiState
import com.sanaa.presentation.model.TvShowUiState
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import com.sanaa.presentation.util.DateTimeUtils.formatLocalizedDate
import entity.Episode
import entity.MediaHistoryItem
import entity.Season
import entity.TvShow
import kotlinx.datetime.Clock
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun TvShow.toState(trailerUrl: String? = null) = TvShowUiState(
    id = id,
    title = title,
    posterPath = posterImageUrl,
    overview = overview,
    rating = String.format("%.1f", imdbRating),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
    genres = genres.map { it.toState() },
    releaseDate = if (releaseDate != defaultDate) releaseDate.toString() else "",
)

fun Season.toState() = SeasonUiState(
    seasonNumber = number,
    episodeCount = episodes.size,
    episodes = episodes.map { it.toState() },
)

@SuppressLint("DefaultLocale")
fun Episode.toState() = EpisodeUiState(
    number = number,
    title = title,
    rating = String.format("%.1f", imdbRating),
    airDate = if (releaseDate != defaultDate) formatLocalizedDate(releaseDate) else null,
    stillPath = stillImagePath,
    duration = durationMinutes.takeIf { it > 0 },
    overview = overview.takeIf(String::isNotBlank),
    seasonNumber = seasonNumber,
)

fun TvShow.toHistory(): MediaHistoryItem {
    return MediaHistoryItem(
        id = id,
        genres = genres,
        posterImageUrl = posterImageUrl,
        mediaType = MediaType.TV_SHOW,
        lastWatchedAt = Clock.System.now().toEpochMilliseconds(),
    )
}