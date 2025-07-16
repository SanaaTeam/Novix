package com.sanaa.presentation.mapper

import com.sanaa.presentation.model.CastMemberUiModel
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.presentation.model.SimilarMovieUiModel
import entity.Movie

fun Movie.toUiModel(
    cast: List<CastMemberUiModel> = emptyList(),
    similarMovies: List<SimilarMovieUiModel> = emptyList(),
    isBookmarked: Boolean = false,
    posterUrls: List<String> = listOf(posterImageUrl)
): MovieDetailsUiModel {
    return MovieDetailsUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = "%.1f".format(imdbRating),
        releaseDate = "${releaseDate.year}-${releaseDate.monthNumber.toString().padStart(2, '0')}-${releaseDate.dayOfMonth.toString().padStart(2, '0')}",
        duration = "${duration}m",
        posterUrls = posterUrls,
        genres = genres.map { it.name },
        cast = cast,
        similarMovies = similarMovies,
        isBookmarked = isBookmarked
    )
}
