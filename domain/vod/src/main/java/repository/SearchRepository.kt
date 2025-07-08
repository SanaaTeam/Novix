package repository

import model.Actor
import model.Language
import model.MediaFilters
import model.Movie
import model.TvSeries

interface SearchRepository {
    suspend fun searchActors(query: String, language: Language): List<Actor>
    suspend fun searchMovies(query: String, filters: MediaFilters?, language: Language): List<Movie>
    suspend fun searchTvSeries(query: String, filters: MediaFilters?, language: Language): List<TvSeries>
}
