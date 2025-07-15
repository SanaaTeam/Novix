package usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import repository.SearchPagingRepository
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchMoviesPagingUseCase(
    private val searchPagingRepository: SearchPagingRepository
) {
    operator fun invoke(
        query: String,
        filters: MediaFilters?
    ): Flow<PagingData<SearchMediaOutput>> {
        return searchPagingRepository.searchMovies(query, filters)
    }
} 