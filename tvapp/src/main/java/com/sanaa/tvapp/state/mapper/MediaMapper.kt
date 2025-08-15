package com.sanaa.tvapp.state.mapper

import android.annotation.SuppressLint
import com.sanaa.tvapp.state.MediaItem
import com.sanaa.tvapp.state.MediaTypeUi
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUi = MediaTypeUi.MOVIE,
    overview = overview,
)

@SuppressLint("DefaultLocale")
fun TvShow.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUi = MediaTypeUi.TV_SHOW,
    overview = overview,
)

fun MediaHistoryItem.toState(): MediaItem = MediaItem(
    id = id,
    title = "",
    imageUrl = posterImageUrl,
    rating = "",
    mediaTypeUi = mediaType.toState(),
    overview = "",
)

fun MediaType.toState(): MediaTypeUi = when (this) {
    MediaType.MOVIE -> MediaTypeUi.MOVIE
    MediaType.TV_SHOW -> MediaTypeUi.TV_SHOW
}