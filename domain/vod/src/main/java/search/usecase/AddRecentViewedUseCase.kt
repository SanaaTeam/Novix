package search.usecase

import search.repository.SearchHistoryRepository
import search.usecase.search_param.RecentViewedMedia
class AddRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(item: RecentViewedMedia) = historyRepo.addRecentViewedMedia(item)
}