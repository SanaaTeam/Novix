package repository

import usecase.search.RecentViewedItem
import usecase.search.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
    suspend fun removeSearchHistoryItem(item: SearchHistoryItem)

    suspend fun getRecentViewed(): List<RecentViewedItem>
    suspend fun addRecentViewedItem(id: String)
    suspend fun clearRecentViewed()
}