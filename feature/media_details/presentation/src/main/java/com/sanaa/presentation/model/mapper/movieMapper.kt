package com.sanaa.presentation.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.MovieUiModel
import entity.MediaHistoryItem
import entity.Movie
import kotlinx.datetime.Clock
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toUiModel(
    isBookmarked: Boolean = false,
    trailerUrl: String? = null,
): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        overview = overview.toString(),
        rating = String.format("%.1f", imdbRating),
        releaseDate = releaseDate.toString(),
        duration = duration,
        genres = genres.map { it.toUiModel() },
        isBookmarked = isBookmarked,
        trailerUrl = trailerUrl,
        posterUrl = posterImageUrl
    )
}

fun Movie.toHistory(): MediaHistoryItem {
    return MediaHistoryItem(
        id = id,
        genres = genres,
        posterImageUrl = posterImageUrl,
        mediaType = MediaType.MOVIE,
        lastWatchedAt = Clock.System.now()
    )
}