package entity

import kotlinx.datetime.LocalDate

data class Review(
    val id: Int,
    val authorName: String?,
    val userHandle: String?,
    val avatarUrl: String?,
    val rating: Float?,
    val content: String,
    val createdDate: LocalDate
)
