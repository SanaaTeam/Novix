package usecase

import repository.SearchRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend fun execute(query: String): List<SearchActorOutput> {
        return searchRepository.searchActors(query)
    }
}
