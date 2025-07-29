package com.sanaa.presentation.model

import android.annotation.SuppressLint
import com.sanaa.presentation.util.formatDateLocalizedDigits
import entity.Movie
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val overview: String? = null,
    val rating: String? = null,
    val releaseDate: String = "",
    val duration: Duration? = null,
    val posterUrls: List<String> = emptyList(),
    val genres: List<GenreUiModel> = emptyList(),
    val isBookmarked: Boolean = false,
    val trailerUrl: String? = null,
    val posterUrl: String? = null
)


@SuppressLint("DefaultLocale")
fun Movie.toUiModel(
    isBookmarked: Boolean = false,
    trailerUrl: String? = null,
    hourLabel: String = "h",
    minuteLabel: String = "m"
): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = String.format("%.1f", imdbRating),
        releaseDate = releaseDate.formatDateLocalizedDigits(),
        duration = duration,
        genres = genres.map { it.toUiModel() },
        isBookmarked = isBookmarked,
        trailerUrl = trailerUrl,
        posterUrl = posterImageUrl
    )
}
