package search.usecase

import search.repository.SearchHistoryRepository
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchTvSeriesOutput

class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun execute(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page, filters)
    }
}