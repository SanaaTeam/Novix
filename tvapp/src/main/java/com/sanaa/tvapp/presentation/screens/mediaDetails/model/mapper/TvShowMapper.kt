package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import android.annotation.SuppressLint
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.EpisodeUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.SeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel
import com.sanaa.tvapp.util.formatLocalizedDate
import entity.Episode
import entity.Season
import entity.TvShow


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