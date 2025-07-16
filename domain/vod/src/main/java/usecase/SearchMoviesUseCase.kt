package usecase

import repository.SearchHistoryRepository
import repository.SearchPagingRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchMediaOutput

class SearchMoviesUseCase(
    private val searchRepository: SearchPagingRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, page: Int, filters: MediaFilters?): List<SearchMediaOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page, filters, MediaType.MOVIE)
    }
}
