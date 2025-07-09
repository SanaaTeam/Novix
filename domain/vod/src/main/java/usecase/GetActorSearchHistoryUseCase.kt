package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchActorHistoryOutput
import usecase.search.SearchMovieHistoryOutput

class GetActorSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchActorHistoryOutput> = historyRepo.getActorSearchHistory()
}
