package search.usecase

import search.repository.SearchHistoryRepository
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchMediaOutput

class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMediaOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMedia(query, filters, MediaType.TV_SERIES)
    }
}