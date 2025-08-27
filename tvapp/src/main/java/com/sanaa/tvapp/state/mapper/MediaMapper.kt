package com.sanaa.tvapp.state.mapper

import android.annotation.SuppressLint
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.MediaTypeUiState
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    imdbRating = String.format("%.1f", imdbRating),
    userRating = rating.toString(),
    mediaTypeUiState = MediaTypeUiState.MOVIE,
    overview = overview,
)

@SuppressLint("DefaultLocale")
fun TvShow.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    imdbRating = String.format("%.1f", imdbRating),
    userRating = rating.toString(),
    mediaTypeUiState = MediaTypeUiState.TV_SHOW,
    overview = overview,
)

fun MediaHistoryItem.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    imdbRating = "",
    mediaTypeUiState = mediaType.toState(),
    overview = "",
)

fun MediaType.toState(): MediaTypeUiState = when (this) {
    MediaType.MOVIE -> MediaTypeUiState.MOVIE
    MediaType.TV_SHOW -> MediaTypeUiState.TV_SHOW
}