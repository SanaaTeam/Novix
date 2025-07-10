package repository

import kotlinx.coroutines.flow.Flow
import usecase.search.RecentViewedItem
import usecase.search.SearchHistory

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryItem(id: Int)
    suspend fun getRecentViewed(): Flow<List<RecentViewedItem>>
    suspend fun addRecentViewedItem(item: RecentViewedItem)
    suspend fun clearRecentViewed()
}