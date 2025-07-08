package usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Language
import model.MediaFilters
import model.Movie
import model.SearchCategory
import model.SearchHistoryItem
import repository.MovieRepository
import repository.SearchHistoryRepository

class SearchMoviesUseCase(
    private val movieRepo: MovieRepository,
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
        return movieRepo.searchMovies(query, filters, language)
    }
}
