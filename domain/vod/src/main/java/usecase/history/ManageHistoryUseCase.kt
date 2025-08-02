package usecase.history

import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.history.history_param.SearchHistory
import javax.inject.Inject

class ManageHistoryUseCase @Inject constructor(
    private val historyRepo: HistoryRepository,
) {
    suspend fun getSearchHistory(): Flow<List<SearchHistory>> =
        historyRepo.getSearchHistory(HISTORY_ITEM_LIMIT)

    suspend fun removeSearchHistory(id: Int) = historyRepo.removeSearchHistoryById(id)
    suspend fun clearSearchHistory() = historyRepo.clearSearchHistory()

    companion object {
        private const val HISTORY_ITEM_LIMIT = 10
    }
}