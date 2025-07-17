package entity
import kotlinx.datetime.LocalDate

data class Movie(
    val id: Int,
    val posterImageUrl: String,
    val title: String,
    val genres: List<Genre>,
    val imdbRating: Float,
    val duration: Int,
    val releaseDate: LocalDate,
    val overview: String,
    val trailerUrl: String? = null
)