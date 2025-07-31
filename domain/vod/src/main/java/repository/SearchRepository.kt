package repository

import entity.Actor
import entity.Movie
import entity.TvSeries
import usecase.search.search_param.MediaFilters


interface SearchRepository {
    suspend fun searchActors(query: String, page: Int): SearchResult<Actor>
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<Movie>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<TvSeries>

    data class SearchResult<T>(
        val totalPages: Int,
        val results: List<T>
    )
}