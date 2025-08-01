package usecase.search

import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchHistoryRepository
import repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    suspend fun searchMovies(query: String, page: Int): List<Movie> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page)
    }

    suspend fun searchActors(query: String, page: Int): List<Actor> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }

    suspend fun searchTvShows(
        query: String,
        page: Int,
    ): List<TvSeries> {
        searchHistoryRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page)
    }
}