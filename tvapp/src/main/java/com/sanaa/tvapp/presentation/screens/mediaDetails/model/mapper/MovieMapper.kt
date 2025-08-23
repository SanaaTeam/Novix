package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import android.annotation.SuppressLint
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import entity.MediaHistoryItem
import entity.Movie
import kotlinx.datetime.Clock
import usecase.search.search_param.MediaType

@SuppressLint("DefaultLocale")
fun Movie.toDetailsUiModel(
    trailerUrl: String? = null,
): MovieDetailsUiModel {
    return MovieDetailsUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = String.format("%.1f", imdbRating),
        releaseDate = releaseDate.toString(),
        duration = duration,
        genres = genres.map { it.toUiModel() },
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
        lastWatchedAt = Clock.System.now().toEpochMilliseconds(),
        title = title,
    )
}