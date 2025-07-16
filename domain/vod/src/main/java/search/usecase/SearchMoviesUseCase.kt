package search.usecase

import search.repository.SearchHistoryRepository
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchMovieOutput

class SearchMoviesUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMovieOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, filters)
    }
}
