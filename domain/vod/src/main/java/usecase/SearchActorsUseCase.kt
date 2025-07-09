package usecase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import entity.Actor
import entity.Language
import entity.SearchCategory
import entity.SearchHistoryItem
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
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        return searchRepository.searchActors(query, language)
    }
}
