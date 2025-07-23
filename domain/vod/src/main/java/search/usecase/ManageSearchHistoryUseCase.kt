package search.usecase

import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow
import search.repository.HistoryRepository
import search.usecase.search_param.SearchHistory

class ManageSearchHistoryUseCase(
    private val historyRepo: HistoryRepository,
) {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>> = historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)
    suspend fun removeSearchHistory(id: Int) = historyRepo.removeSearchHistoryById(id)
    suspend fun clearSearchHistory() = historyRepo.clearSearchHistory()
    suspend fun getWatchedMoviesHistory(): List<Movie> = historyRepo.getWatchedMoviesHistory()
    suspend fun getWatchedSeriesHistory(): List<TvSeries> = historyRepo.getWatchedSeriesHistory()

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}
