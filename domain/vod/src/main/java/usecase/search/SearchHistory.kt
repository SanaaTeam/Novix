package usecase.search

import kotlinx.datetime.LocalDateTime

data class SearchHistory (
    val query: String,
    val timestamp: LocalDateTime
)