package search.usecase

import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchMediaOutput

class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMediaOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMedia(query, filters, MediaType.TV_SERIES)
    }
}