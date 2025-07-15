package search.usecase

import search.repository.SearchHistoryRepository
import search.repository.SearchRepository
import search.usecase.search_param.SearchActorOutput

class SearchActorsUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String): List<SearchActorOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query)
    }
}
