package usecase

import entity.Language
import entity.TvSeries
import extensions.now
import kotlinx.datetime.LocalDateTime
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.SearchCategory
import usecase.search.SearchHistoryInputItem
import usecase.search.SearchMediaOutput

class SearchTvSeriesUseCase(
    private val searchRepository: SearchRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, filters: MediaFilters?, language: Language): List<SearchMediaOutput> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryInputItem(
                query = query,
                category = SearchCategory.TV_SERIES,
                filters = filters,
                language = language,
                timestamp = LocalDateTime.now()
            )
        )
        return searchRepository.searchTvSeries(query, filters, language)
    }
}