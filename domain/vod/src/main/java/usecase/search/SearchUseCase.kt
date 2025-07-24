package usecase.search

import repository.HistoryRepository
import repository.SearchRepository
import usecase.search.search_param.MediaFilters
import usecase.search.search_param.SearchActorOutput
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput

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
