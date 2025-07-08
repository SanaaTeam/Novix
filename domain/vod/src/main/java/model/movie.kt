package model
import kotlinx.datetime.LocalDate

data class Movie(
    val id: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val actors: List<String>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?
)