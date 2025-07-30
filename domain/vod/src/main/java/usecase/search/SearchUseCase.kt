package usecase.search

import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.search_param.MediaFilters
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun searchMovies(query: String, page: Int, filters: MediaFilters?): List<Movie> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page, filters)
    }

    suspend fun searchActors(query: String, page: Int): List<Actor> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<TvSeries> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page, filters)
    }
}
