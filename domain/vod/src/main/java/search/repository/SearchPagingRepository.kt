package search.repository

import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMediaOutput

interface SearchPagingRepository {
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType
    ): List<SearchMediaOutput>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType
    ): List<SearchMediaOutput>

    suspend fun searchActors(query: String, page: Int): List<SearchActorOutput>

}