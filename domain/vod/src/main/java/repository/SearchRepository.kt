package repository

import entity.Actor
import entity.Movie
import entity.TvSeries
import usecase.search.search_param.MediaFilters


interface SearchRepository {
    suspend fun searchActors(query: String, page: Int): List<Actor>
    suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<Movie>

    suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<TvSeries>
}