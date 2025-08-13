package entity

import kotlinx.datetime.LocalDate

data class Actor(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val department: String,
    val character: String,
    val birthDate: LocalDate,
    val deathDate: LocalDate,
    val biography: String
)