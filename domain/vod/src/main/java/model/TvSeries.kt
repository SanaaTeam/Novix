package model
import kotlinx.datetime.LocalDate

data class TvSeries(
    val id: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val seasons: List<Season>,
    val actors: List<String>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?,
)
data class Season(
    val id: String,
    val title: String,
    val number: Int,
    val episodes: List<Episode>
)

data class Episode(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val releaseDate: LocalDate
)