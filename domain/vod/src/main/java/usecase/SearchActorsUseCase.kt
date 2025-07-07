package usecase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Actor
import model.SearchCategory
import model.SearchHistoryItem
import repository.ActorRepository
import repository.SearchHistoryRepository


class SearchActorsUseCase(
    private val actorRepo: ActorRepository,
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(query: String, language: String): List<Actor> {
        historyRepo.addSearchHistoryItem(
            SearchHistoryItem(
                query,
                SearchCategory.ACTOR,
                null,
                language,
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        return actorRepo.searchActors(query, language)
    }
}
