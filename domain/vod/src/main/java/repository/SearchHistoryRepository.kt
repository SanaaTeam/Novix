package repository

import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia

interface SearchHistoryRepository {
    suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryById(id: Int)
    suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>>
    suspend fun addRecentViewedMedia(item: RecentViewedMedia)
    suspend fun clearRecentViewed()
    suspend fun getWatchedMoviesHistory(page: Int, genreId: Int?): List<Movie>
    suspend fun getWatchedSeriesHistory(page: Int, genreId: Int?): List<TvSeries>
}