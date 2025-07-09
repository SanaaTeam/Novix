package usecase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import entity.Actor
import entity.Language
import extensions.now
import kotlinx.datetime.LocalDateTime
import usecase.search.SearchCategory
import usecase.search.SearchHistoryItem
import repository.SearchRepository
import repository.SearchHistoryRepository


class SearchActorsUseCase(
    private val searchRepository: SearchRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, language: Language): List<Actor> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryItem(
                query,
                SearchCategory.ACTOR,
                null,
                language,
                LocalDateTime.now()
            )
        )
        return searchRepository.searchActors(query, language)
    }
}
