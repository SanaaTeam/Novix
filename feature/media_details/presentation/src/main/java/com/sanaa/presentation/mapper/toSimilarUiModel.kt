package com.sanaa.presentation.mapper

import com.sanaa.presentation.model.SimilarMovieUiModel
import entity.Movie

fun Movie.toSimilarUiModel(isBookmarked: Boolean = false): SimilarMovieUiModel {
    return SimilarMovieUiModel(
        id = id,
        posterUrl = posterImageUrl,
        title = title,
        isBookmarked = isBookmarked
    )
}