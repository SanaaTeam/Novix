package usecase.history

import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.history.history_param.SearchHistory

class ManageHistoryUseCase(
    private val historyRepo: HistoryRepository,
) {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>> =
        historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)

    suspend fun removeSearchHistory(id: Int) = historyRepo.removeSearchHistoryById(id)
    suspend fun clearSearchHistory() = historyRepo.clearSearchHistory()
    suspend fun getWatchedMoviesHistory(page: Int, genreId: Int?): List<Movie> =
        historyRepo.getWatchedMoviesHistory(page, genreId)

    suspend fun getWatchedSeriesHistory(page: Int, genreId: Int?): List<TvSeries> =
        historyRepo.getWatchedSeriesHistory(page, genreId)

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}