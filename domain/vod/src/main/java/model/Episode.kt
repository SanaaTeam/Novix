package model

import kotlinx.datetime.LocalDate

data class Episode(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val releaseDate: LocalDate
)