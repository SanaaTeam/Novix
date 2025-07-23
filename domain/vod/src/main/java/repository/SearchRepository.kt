package repository

import usecase.search.search_param.MediaFilters
import usecase.search.search_param.SearchActorOutput
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput


interface SearchRepository {
    suspend fun searchActors(query: String, page: Int): List<SearchActorOutput>
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchMovieOutput>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput>
}