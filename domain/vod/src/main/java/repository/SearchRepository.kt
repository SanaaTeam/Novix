package repository

import entity.Actor
import entity.Movie
import entity.TvSeries


interface SearchRepository {
    suspend fun searchActors(query: String, page: Int): List<Actor>
    suspend fun searchMovies(
        query: String,
        page: Int,
    ): List<Movie>

    suspend fun searchTvShows(
        query: String,
        page: Int,
    ): List<TvSeries>
}