package repository

import entity.Actor
import entity.Language
import entity.MediaFilters
import entity.Movie
import entity.TvSeries

interface SearchRepository {
    suspend fun searchActors(query: String, language: Language): List<Actor>
    suspend fun searchMovies(query: String, filters: MediaFilters?, language: Language): List<Movie>
    suspend fun searchTvSeries(query: String, filters: MediaFilters?, language: Language): List<TvSeries>
}
