package repository

import entity.MediaHistoryItem
import kotlinx.coroutines.flow.Flow
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.search_param.MediaType

interface HistoryRepository {
    suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryById(id: Int)
    suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>>
    suspend fun addRecentViewedMedia(item: RecentViewedMedia)
    suspend fun clearRecentViewed()
    suspend fun addWatchedMediaHistory(username: String, media: MediaHistoryItem)
    suspend fun getWatchedMediaHistory(username: String, mediaType: MediaType?, genreId: Int?): Flow<List<MediaHistoryItem>>
}