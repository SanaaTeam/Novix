package repository

import usecase.search.RecentViewedItem
import usecase.search.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
    suspend fun removeSearchHistoryItem(id: String)

    suspend fun getRecentViewed(): List<RecentViewedItem>
    suspend fun addRecentViewedItem(item: RecentViewedItem)
    suspend fun clearRecentViewed()
}