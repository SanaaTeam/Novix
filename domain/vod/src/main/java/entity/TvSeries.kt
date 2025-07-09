package entity
import kotlinx.datetime.LocalDate

data class TvSeries(
    val id: Long,
    val posterImageUrl: String,
    val title: String,
    val description: String,
    val seasons: List<Season>,
    val actorIds: List<String>,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float?,
)
data class Season(
    val id: Long,
    val title: String,
    val number: Int,
    val episodes: List<Episode>
)

data class Episode(
    val id: Long,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val releaseDate: LocalDate
)