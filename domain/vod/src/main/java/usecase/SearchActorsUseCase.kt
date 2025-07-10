package usecase

import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCase(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun execute(query: String): List<SearchActorOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query)
    }
}
