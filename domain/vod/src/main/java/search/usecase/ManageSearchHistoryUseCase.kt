package search.usecase

import kotlinx.coroutines.flow.Flow
import search.repository.SearchHistoryRepository
import search.usecase.search_param.SearchHistory

class ManageSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>> = historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)
    suspend fun removeSearchHistory(id: Int) = historyRepo.removeSearchHistoryById(id)
    suspend fun clearSearchHistory() = historyRepo.clearSearchHistory()

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}
