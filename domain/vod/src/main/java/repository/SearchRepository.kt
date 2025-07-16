package repository

import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

interface SearchRepository {
    suspend fun searchActors(query: String, page: Int): List<SearchActorOutput>
    suspend fun searchMedia(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchMediaOutput>
}