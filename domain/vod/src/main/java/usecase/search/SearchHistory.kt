package usecase.search

import kotlinx.datetime.LocalDateTime

data class SearchHistory (
    val id: Long? = null,
    val query: String,
    val timestamp: LocalDateTime
)