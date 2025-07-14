package search.repository

import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMediaOutput


interface SearchRepository {
    suspend fun searchActors(query: String): List<SearchActorOutput>
    suspend fun searchMedia(
        query: String,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchMediaOutput>
}