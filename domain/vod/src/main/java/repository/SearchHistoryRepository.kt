package repository

import usecase.search.RecentViewedItem
import usecase.search.SearchHistoryInputItem
import usecase.search.SearchHistoryOutputItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryOutputItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryInputItem)
    suspend fun removeSearchHistoryItem(id: Long)

    suspend fun getRecentViewed(): List<RecentViewedItem>
    suspend fun addRecentViewedItem(item: RecentViewedItem)
    suspend fun clearRecentViewed()
}