package model
import kotlinx.datetime.LocalDate

data class TvSeries(
    val id: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val seasons: List<String>,
    val actors: List<String>,
    val releaseDate: LocalDate,
    val genres: List<String>,
    val imdbRating: Float?,
)
