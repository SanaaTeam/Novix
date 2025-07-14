package repository

import usecase.search.MediaFilters
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

interface SearchPagingRepository {
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?
    ): List<SearchMediaOutput>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?
    ): List<SearchMediaOutput>

    suspend fun searchActors(
        query: String,
        page: Int
    ): List<SearchActorOutput>
}