package usecase

import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend fun execute(query: String, filters: MediaFilters?): List<SearchMediaOutput> {
        return searchRepository.searchTvSeries(query, filters)
    }
}