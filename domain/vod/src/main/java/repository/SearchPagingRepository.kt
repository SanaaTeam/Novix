package repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import usecase.search.MediaFilters
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

interface SearchPagingRepository {
    fun searchMovies(
        query: String,
        filters: MediaFilters?
    ): Flow<PagingData<SearchMediaOutput>>

    fun searchTvShows(
        query: String,
        filters: MediaFilters?
    ): Flow<PagingData<SearchMediaOutput>>

    fun searchActors(
        query: String
    ): Flow<PagingData<SearchActorOutput>>
}