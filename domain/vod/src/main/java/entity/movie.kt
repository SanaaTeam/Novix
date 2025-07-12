package entity
import kotlinx.datetime.LocalDate

data class Movie(
    val id: Int,
    val posterImageUrl: String,
    val title: String,
    val description: String,
    val actorIds: List<Int>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?
)