package repository

import usecase.search.RecentViewedItem
import usecase.search.SearchHistoryInputItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryInputItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryInputItem)
    suspend fun removeSearchHistoryItem(id: Long)

    suspend fun getRecentViewed(): List<RecentViewedItem>
    suspend fun addRecentViewedItem(item: RecentViewedItem)
    suspend fun clearRecentViewed()
}