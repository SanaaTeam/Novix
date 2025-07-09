package repository

import usecase.search.RecentViewedItem
import usecase.search.SearchActorHistoryOutput
import usecase.search.SearchHistoryInputItem
import usecase.search.SearchMovieHistoryOutput

interface SearchHistoryRepository {
    suspend fun getMovieSearchHistory(): List<SearchMovieHistoryOutput>
    suspend fun getActorSearchHistory(): List<SearchActorHistoryOutput>
    suspend fun clearSearchHistory()
    suspend fun addSearchHistoryItem(item: SearchHistoryInputItem)
    suspend fun removeSearchHistoryItem(id: Long)

    suspend fun getRecentViewed(): List<RecentViewedItem>
    suspend fun addRecentViewedItem(item: RecentViewedItem)
    suspend fun clearRecentViewed()
}