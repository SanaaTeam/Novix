package usecase.search

import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchHistoryRepository
import repository.SearchRepository
import repository.SearchRepository.SearchResult
import usecase.search.search_param.MediaFilters
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun searchMovies(query: String, page: Int, filters: MediaFilters?): SearchResult<Movie> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page, filters)
    }

    suspend fun searchActors(query: String, page: Int): SearchResult<Actor> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<TvSeries> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page, filters)
    }
}
