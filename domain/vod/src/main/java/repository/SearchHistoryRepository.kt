package repository

import entity.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
}