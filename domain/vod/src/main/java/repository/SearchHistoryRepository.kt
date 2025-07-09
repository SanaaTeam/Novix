package repository

import usecase.search.RecentWatchingItem
import usecase.search.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<SearchHistoryItem>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
    suspend fun removeSearchHistoryItem(item: SearchHistoryItem)

    suspend fun getRecentWatching(): List<RecentWatchingItem>
    suspend fun addRecentWatching(item: RecentWatchingItem)
    suspend fun clearRecentWatching()
}