package usecase.search

import entity.Actor
import entity.Movie
import entity.TvShow
import repository.HistoryRepository
import repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val historyRepository: HistoryRepository,
) {
    suspend fun searchMovies(query: String, page: Int): List<Movie> {
        historyRepository.addSearchHistory(query)
        return searchRepository.searchMovies(query, page)
    }

    suspend fun searchActors(query: String, page: Int): List<Actor> {
        historyRepository.addSearchHistory(query)
        return searchRepository.searchActors(query, page)
    }

    suspend fun searchTvShows(
        query: String,
        page: Int,
    ): List<TvShow> {
        historyRepository.addSearchHistory(query)
        return searchRepository.searchTvShows(query, page)
    }
}