package com.sanaa.presentation.model


data class MovieDetailsUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val rating: String,
    val releaseDate: String,
    val duration: String,
    val posterUrls: List<String>,
    val genres: List<String>,
    val cast: List<CastMemberUiModel>,
    val similarMovies: List<SimilarMovieUiModel>,
    val isBookmarked: Boolean
)
