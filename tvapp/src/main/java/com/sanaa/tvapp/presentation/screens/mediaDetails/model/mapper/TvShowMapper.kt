package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import android.annotation.SuppressLint
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.EpisodeUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.SeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel
import com.sanaa.tvapp.util.formatLocalizedDate
import entity.Episode
import entity.MediaHistoryItem
import entity.Season
import entity.TvShow
import kotlinx.datetime.Clock
import usecase.search.search_param.MediaType


@SuppressLint("DefaultLocale")
fun TvShow.toTvShowUiModel(trailerUrl: String? = null) = TvShowDetailsUiModel(
    id = id,
    title = title,
    posterUrl = posterImageUrl,
    overview = overview,
    rating = String.format("%.1f", imdbRating),
    seasonsCount = seasonsCount,
    trailerUrl = trailerUrl,
    genres = genres.map { it.toUiModel() },
    releaseDate = releaseDate.toString(),
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

fun TvShow.toHistory(): MediaHistoryItem {
    return MediaHistoryItem(
        id = id,
        genres = genres,
        posterImageUrl = posterImageUrl,
        mediaType = MediaType.TV_SHOW,
        lastWatchedAt = Clock.System.now().toEpochMilliseconds(),
        title = title,
    )
}