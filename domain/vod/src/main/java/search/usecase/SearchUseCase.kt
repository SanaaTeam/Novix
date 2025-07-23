package search.usecase

import search.repository.HistoryRepository
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: HistoryRepository,
) {
    suspend fun searchMovies(query: String, page: Int, filters: MediaFilters?): List<SearchMovieOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page, filters)
    }

    suspend fun searchActors(query: String, page: Int): List<SearchActorOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page, filters)
    }
}
