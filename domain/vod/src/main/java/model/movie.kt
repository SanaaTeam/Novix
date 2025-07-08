package model
import kotlinx.datetime.LocalDate

data class Movie(
    val id: String,
    val posterImageUrl: String,
    val title: String,
    val description: String,
    val actorIds: List<String>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?
)