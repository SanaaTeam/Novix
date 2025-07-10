package usecase

import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchMediaOutput

class SearchMoviesUseCase(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMediaOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMedia(query, filters, MediaType.MOVIE)
    }
}
