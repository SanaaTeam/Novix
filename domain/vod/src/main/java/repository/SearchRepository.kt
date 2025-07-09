package repository

import entity.Language
import usecase.search.MediaFilters
import usecase.search.SearchActorHistoryOutput
import usecase.search.SearchMediaHistoryOutput

interface SearchRepository {
    suspend fun searchActors(query: String, language: Language): List<SearchActorHistoryOutput>
    suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
        language: Language
    ): List<SearchMediaHistoryOutput>

    suspend fun searchTvSeries(
        query: String,
        filters: MediaFilters?,
        language: Language
    ): List<SearchMediaHistoryOutput>
}