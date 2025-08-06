package com.sanaa.presentation.state.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import entity.MediaHistoryItem
import entity.Movie
import entity.TvSeries
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUi = MediaTypeUi.MOVIE,
    isSaved = isSaved
)

@SuppressLint("DefaultLocale")
fun TvSeries.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUi = MediaTypeUi.TV_SHOW
)

fun MediaHistoryItem.toState(): MediaItem = MediaItem(
    id = id,
    title = "",
    imageUrl = posterImageUrl,
    rating = "",
    mediaTypeUi = mediaType.toState()
)

fun MediaType.toState(): MediaTypeUi = when (this) {
    MediaType.MOVIE -> MediaTypeUi.MOVIE
    MediaType.TV_SERIES -> MediaTypeUi.TV_SHOW
}