package com.sanaa.presentation.state.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import entity.Movie
import entity.TvSeries
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase.MediaHistoryItem

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaType = MediaType.MOVIE
)

@SuppressLint("DefaultLocale")
fun TvSeries.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaType = MediaType.TV_SHOW
)

fun MediaHistoryItem.toState(): MediaItem = MediaItem(
    id = id,
    title = "",
    imageUrl = posterImageUrl,
    rating = "",
    mediaType = mediaType.toState()
)

fun entity.MediaType.toState(): MediaType = when (this) {
    entity.MediaType.MOVIE -> MediaType.MOVIE
    entity.MediaType.TV_SERIES -> MediaType.TV_SHOW
}