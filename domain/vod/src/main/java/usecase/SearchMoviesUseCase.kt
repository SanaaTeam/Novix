package usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import entity.Language
import entity.MediaFilters
import entity.Movie
import entity.SearchCategory
import entity.SearchHistoryItem
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
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        return searchRepository.searchMovies(query, filters, language)
    }
}
