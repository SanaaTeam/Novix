package usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import repository.SearchPagingRepository
import usecase.search.SearchActorOutput

class SearchActorsPagingUseCase(
    private val searchPagingRepository: SearchPagingRepository
) {
    operator fun invoke(query: String): Flow<PagingData<SearchActorOutput>> {
        return searchPagingRepository.searchActors(query)
    }
} 