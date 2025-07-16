package usecase

import repository.SearchHistoryRepository
import repository.SearchPagingRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCase(
    private val searchRepository: SearchPagingRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, page: Int): List<SearchActorOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }
}
