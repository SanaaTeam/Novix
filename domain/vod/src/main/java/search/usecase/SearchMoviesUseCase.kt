package search.usecase

import search.repository.SearchHistoryRepository
import search.repository.SearchPagingRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchMediaOutput

class SearchMoviesUseCase(
    private val searchRepository: SearchPagingRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, page: Int, filters: MediaFilters?): List<SearchMediaOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page, filters, MediaType.MOVIE)
    }
}
