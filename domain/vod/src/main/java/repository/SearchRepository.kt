package repository

import entity.Language
import usecase.search.MediaFilters
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

interface SearchRepository {
    suspend fun searchActors(query: String, language: Language): List<SearchActorOutput>
    suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
        language: Language
    ): List<SearchMediaOutput>

    suspend fun searchTvSeries(
        query: String,
        filters: MediaFilters?,
        language: Language
    ): List<SearchMediaOutput>
}