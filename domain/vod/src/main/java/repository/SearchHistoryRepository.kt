package repository

import usecase.params.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
    suspend fun removeSearchHistoryItem(item: SearchHistoryItem)
}