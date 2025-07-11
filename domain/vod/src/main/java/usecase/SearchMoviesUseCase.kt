package usecase

import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchMoviesUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMediaOutput> {
        return searchRepository.searchMovies(query, filters)
    }
}
