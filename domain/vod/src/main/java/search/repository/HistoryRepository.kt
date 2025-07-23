package search.repository

import entity.Genre
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow
import search.usecase.ManageRecentViewedUseCase.RecentViewedMedia
import search.usecase.search_param.SearchHistory

interface HistoryRepository {
    suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(query: String)
    suspend fun clearSearchHistory()
    suspend fun removeSearchHistoryById(id: Int)
    suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>>
    suspend fun addRecentViewedMedia(item: RecentViewedMedia)
    suspend fun clearRecentViewed()
    suspend fun getWatchedMoviesHistory(genre: Genre): List<Movie>
    suspend fun getWatchedSeriesHistory(genre: Genre): List<TvSeries>
}