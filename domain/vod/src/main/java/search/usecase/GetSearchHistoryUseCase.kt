package search.usecase

import kotlinx.coroutines.flow.Flow
import search.repository.SearchHistoryRepository
import search.usecase.search_param.SearchHistory

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun execute(): Flow<List<SearchHistory>> =
        historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}
