package repository

import model.Language
import model.MediaFilters
import model.Movie

interface MovieRepository {
    suspend fun searchMovies(query: String, filters: MediaFilters?, language: Language): List<Movie>
    // other movie-related methods (details, etc.)
}