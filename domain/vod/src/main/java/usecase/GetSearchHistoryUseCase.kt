package usecase

import kotlinx.coroutines.flow.Flow
import repository.SearchHistoryRepository
import usecase.search.SearchHistory

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): Flow<List<SearchHistory>> = historyRepo.getSearchHistory()
}
