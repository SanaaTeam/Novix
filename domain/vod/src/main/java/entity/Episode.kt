package entity

import kotlinx.datetime.LocalDate

data class Episode(
    val id: Int,
    val title: String,
    val number: Int,
    val seasonNumber: Int,
    val imdbRating: Float,
    val overview: String,
    val durationMinutes: Int,
    val releaseDate: LocalDate,
    val stillImagePath: String,
    val rating: Int
)