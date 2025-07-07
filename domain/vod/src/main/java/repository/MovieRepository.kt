package repository

import model.MediaFilters
import model.Movie

interface MovieRepository {
    suspend fun searchMovies(query: String, filters: MediaFilters?, language: String): List<Movie>
    // other movie-related methods (details, etc.)
}