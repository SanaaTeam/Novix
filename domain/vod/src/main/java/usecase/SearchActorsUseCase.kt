package usecase

import entity.Actor
import entity.Language
import extensions.now
import kotlinx.datetime.LocalDateTime
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.SearchCategory
import usecase.search.SearchHistoryItem

class SearchActorsUseCase(
    private val searchRepository: SearchRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, language: Language): List<Actor> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryItem(
                query = query,
                category = SearchCategory.MOVIE,
                filters = null,
                language = language,
                timestamp = LocalDateTime.now()
            )
        )
        return searchRepository.searchActors(query, language)
    }
}
