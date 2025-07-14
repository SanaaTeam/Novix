package entity

import kotlinx.datetime.LocalDate

data class TvSeries(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: LocalDate,
    val genres: List<Genre>,
    val imdbRating: Float,
    val posterImageUrl: String,
    val seasonsCount: Int,
)

data class Season(
    val id: Int,
    val title: String,
    val overview: String,
    val number: Int,
    val episodes: List<Episode>,
)

data class Episode(
    val id: Int,
    val title: String,
    val number: Int,
    val seasonNumber: Int,
    val imdbRating: Float,
    val overview: String,
    val durationMinutes: Int,
    val releaseDate: LocalDate
)