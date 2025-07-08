package usecase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Language
import model.MediaFilters
import model.SearchCategory
import model.SearchHistoryItem
import model.TvSeries
import repository.SearchHistoryRepository
import repository.SearchRepository


class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, filters: MediaFilters?, language: Language): List<TvSeries> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryItem(
                query,
                SearchCategory.TV_SERIES,
                filters,
                language,
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        return searchRepository.searchTvSeries(query, filters, language)
    }
}
