package search.repository

import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput

interface SearchPagingRepository {
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchMovieOutput>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchTvSeriesOutput>

    suspend fun searchActors(query: String, page: Int): List<SearchActorOutput>

}