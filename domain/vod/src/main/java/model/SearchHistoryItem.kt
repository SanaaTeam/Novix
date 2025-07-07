package model

import kotlinx.datetime.LocalDateTime

data class SearchHistoryItem(
    val query: String,
    val category: SearchCategory,
    val filters: MediaFilters? = null,
    val language: String,
    val timestamp: LocalDateTime
)