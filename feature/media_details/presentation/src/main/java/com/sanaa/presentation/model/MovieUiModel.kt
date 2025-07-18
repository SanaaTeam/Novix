package com.sanaa.presentation.model

import android.annotation.SuppressLint
import com.sanaa.presentation.util.formatDateLocalizedDigits
import entity.Movie

data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val rating: String = "",
    val releaseDate: String = "",
    val duration: Int? = null,
    val posterUrls: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val isBookmarked: Boolean = false,
    val trailerUrl: String? = null,
    val posterUrl: String? = null
)


@SuppressLint("DefaultLocale")
fun Movie.toUiModel(
    isBookmarked: Boolean = false,
    trailerUrl: String? = null
): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = String.format("%.1f", imdbRating),
        releaseDate = releaseDate.formatDateLocalizedDigits(),
        duration = duration,
        genres = genres.map { it.name },
        isBookmarked = isBookmarked,
        trailerUrl = trailerUrl,
        posterUrl = posterImageUrl
    )
}
