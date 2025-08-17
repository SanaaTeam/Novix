package com.sanaa.presentation.state.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.PlaylistUiItem
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import usecase.custom_list.custom_list_param.SavedList
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUiState = MediaTypeUiState.MOVIE,
)

@SuppressLint("DefaultLocale")
fun TvShow.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUiState = MediaTypeUiState.TV_SHOW
)

fun MediaHistoryItem.toState(): MediaItemUiState = MediaItemUiState(
    id = id,
    title = "",
    imageUrl = posterImageUrl,
    rating = "",
    mediaTypeUiState = mediaType.toState(),
)

fun MediaType.toState(): MediaTypeUiState = when (this) {
    MediaType.MOVIE -> MediaTypeUiState.MOVIE
    MediaType.TV_SHOW -> MediaTypeUiState.TV_SHOW
}

fun SavedList.toState(): PlaylistUiItem {
    return PlaylistUiItem(title = title, itemCount = itemCount, id = id.toLong(), itemsIds = itemsIds.map { it.toLong() })
}

fun List<SavedList>.toState(): List<PlaylistUiItem> {
    return map { it.toState() }
}