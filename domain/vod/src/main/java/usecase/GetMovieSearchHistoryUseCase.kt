package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchMovieHistoryOutput

class GetMovieSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchMovieHistoryOutput> = historyRepo.getMovieSearchHistory()
}
