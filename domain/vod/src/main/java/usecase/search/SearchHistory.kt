package usecase.search

import kotlinx.datetime.LocalDateTime

data class SearchHistory (
    val id: Int,
    val query: String,
    val timestamp: LocalDateTime
)