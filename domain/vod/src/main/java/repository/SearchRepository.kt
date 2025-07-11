package repository

import usecase.search.MediaFilters
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

interface SearchRepository {
    suspend fun searchActors(query: String): List<SearchActorOutput>
    suspend fun searchMovies(query: String, filters: MediaFilters?): List<SearchMediaOutput>
    suspend fun searchTvSeries(query: String, filters: MediaFilters?): List<SearchMediaOutput>
}