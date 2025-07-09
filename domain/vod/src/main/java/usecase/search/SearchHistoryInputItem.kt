package usecase.search

import entity.Language
import kotlinx.datetime.LocalDateTime

data class SearchHistoryInputItem(
    val id: Long? = null,
    val query: String,
    val category: SearchCategory,
    val filters: MediaFilters? = null,
    val language: Language,
    val timestamp: LocalDateTime
)