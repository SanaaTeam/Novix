package repository

import kotlinx.coroutines.flow.Flow
import usecase.search.RecentViewedMedia
import usecase.search.SearchHistory

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryById(id: Int)
    suspend fun getRecentViewed(): Flow<List<RecentViewedMedia>>
    suspend fun addRecentViewedMedia(item: RecentViewedMedia)
    suspend fun clearRecentViewed()
}