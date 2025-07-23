package usecase.history

import entity.Genre
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.history.history_param.SearchHistory

class ManageHistoryUseCase(
    private val historyRepo: HistoryRepository,
) {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>> = historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)
    suspend fun removeSearchHistory(id: Int) = historyRepo.removeSearchHistoryById(id)
    suspend fun clearSearchHistory() = historyRepo.clearSearchHistory()
    suspend fun getWatchedMoviesHistory(genre: Genre): List<Movie> = historyRepo.getWatchedMoviesHistory(genre)
    suspend fun getWatchedSeriesHistory(genre: Genre): List<TvSeries> = historyRepo.getWatchedSeriesHistory(genre)

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}