package repository

import entity.MediaType
import kotlinx.coroutines.flow.Flow
import usecase.history.ManageWatchedMediaHistoryUseCase.MediaHistoryItem
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia

interface HistoryRepository {
    suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryById(id: Int)
    suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>>
    suspend fun addRecentViewedMedia(item: RecentViewedMedia)
    suspend fun clearRecentViewed()
    suspend fun addWatchedMediaHistory(media: MediaHistoryItem)
    suspend fun getWatchedMediaHistory(mediaType: MediaType?, genreId: Int?): List<MediaHistoryItem>
}