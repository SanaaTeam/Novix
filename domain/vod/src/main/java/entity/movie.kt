package entity
import kotlinx.datetime.LocalDate

data class Movie(
    val id: Long,
    val posterImageUrl: String,
    val title: String,
    val description: String,
    val actorIds: List<Long>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?
)