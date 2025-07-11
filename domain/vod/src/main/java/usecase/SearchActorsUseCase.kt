package usecase

import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String): List<SearchActorOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query)
    }
}
