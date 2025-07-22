package home.usecase

import entity.Movie
import entity.TvSeries
import search.repository.HistoryRepository

class GetWatchedHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun getWatchedMoviesHistory(): List<Movie> {
        return historyRepository.getWatchedMoviesHistory()
    }

    suspend fun getWatchedSeriesHistory(): List<TvSeries> {
        return historyRepository.getWatchedSeriesHistory()
    }
}