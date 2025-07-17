package com.sanaa.presentation.module

import entity.Movie


data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String? = null,
    val rating: String = "",
)


fun Movie.toMovieUiModel() = MovieUiModel(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating.toString(),
)