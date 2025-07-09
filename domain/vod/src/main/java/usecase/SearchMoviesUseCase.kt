package usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import entity.Language
import usecase.search.MediaFilters
import entity.Movie
import extensions.now
import kotlinx.datetime.LocalDateTime
import usecase.search.SearchCategory
import usecase.search.SearchHistoryItem
import repository.SearchHistoryRepository
import repository.SearchRepository

class SearchMoviesUseCase(
    private val searchRepository: SearchRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, filters: MediaFilters?, language: Language): List<Movie> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryItem(
                query,
                SearchCategory.MOVIE,
                filters,
                language,
                LocalDateTime.now()
            )
        )
        return searchRepository.searchMovies(query, filters, language)
    }
}
