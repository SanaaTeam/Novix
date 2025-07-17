package search.repository

import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput


interface SearchRepository {
    suspend fun searchActors(query: String): List<SearchActorOutput>
    suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
    ): List<SearchMovieOutput>

    suspend fun searchTvShows(
        query: String,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput>
}