package entity

import kotlinx.datetime.LocalDate
import kotlin.time.Duration

data class Movie(
    val id: Int,
    val posterImageUrl: String,
    val title: String,
    val genres: List<Genre>,
    val imdbRating: Float?,
    val duration: Duration?,
    val releaseDate: LocalDate,
    val overview: String = "",
    val trailerUrl: String? = null,
    val rating: Int?
)