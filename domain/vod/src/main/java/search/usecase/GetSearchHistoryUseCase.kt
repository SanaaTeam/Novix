package search.usecase

import kotlinx.coroutines.flow.Flow
import repository.SearchHistoryRepository
import usecase.search.SearchHistory

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun execute(): Flow<List<SearchHistory>> = historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)

    companion object{
        private const val HISTORY_ITEM_LIMIT = 10
    }
}
