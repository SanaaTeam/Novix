package entity

import kotlinx.datetime.LocalDate

data class TvShow(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float,
    val posterImageUrl: String,
    val seasonsCount: Int,
    val rating: Int
)