package com.sanaa.presentation.state.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.PlaylistUiItem
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import usecase.custom_list.custom_list_param.SavedList
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaTypeUi = MediaTypeUi.MOVIE,
)

@SuppressLint("DefaultLocale")
fun TvShow.toState(): MediaItem = MediaItem(
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
    mediaTypeUi = mediaType.toState(),
)

fun MediaType.toState(): MediaTypeUi = when (this) {
    MediaType.MOVIE -> MediaTypeUi.MOVIE
    MediaType.TV_SHOW -> MediaTypeUi.TV_SHOW
}

fun SavedList.toState(): PlaylistUiItem {
    return PlaylistUiItem(title = title, itemCount = itemCount, id = id.toLong(), itemsIds = itemsIds.map { it.toLong() })
}

fun List<SavedList>.toState(): List<PlaylistUiItem> {
    return map { it.toState() }
}