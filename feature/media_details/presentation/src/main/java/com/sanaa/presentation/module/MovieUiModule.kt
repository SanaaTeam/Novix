package com.sanaa.presentation.module

import entity.Movie

data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val rating: String = "",
    val releaseDate: String = "",
    val duration: String = "",
    val posterUrls: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val isBookmarked: Boolean = false,
    val trailerUrl: String? = null,
    val posterUrl: String? = null
)


fun Movie.toUiModel(
    isBookmarked: Boolean = false,
    trailerUrl: String? = null
): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = "%.1f".format(imdbRating),
        releaseDate = "${releaseDate.year}-${
            releaseDate.monthNumber.toString().padStart(2, '0')
        }-${releaseDate.dayOfMonth.toString().padStart(2, '0')}",
        duration = duration.toString(),
        genres = genres.map { it.name },
        isBookmarked = isBookmarked,
        trailerUrl = trailerUrl,
        posterUrl = posterImageUrl
    )
}
