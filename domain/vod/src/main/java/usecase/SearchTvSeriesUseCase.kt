package usecase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import entity.Language
import usecase.search.MediaFilters
import usecase.search.SearchCategory
import usecase.search.SearchHistoryItem
import entity.TvSeries
import extensions.now
import kotlinx.datetime.LocalDateTime
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
                LocalDateTime.now()
            )
        )
        return searchRepository.searchTvSeries(query, filters, language)
    }
}
