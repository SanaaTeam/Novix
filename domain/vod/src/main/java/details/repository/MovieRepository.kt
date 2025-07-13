package details.repository

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review

interface MovieRepository {
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun getImages(id: Int): List<String>
    suspend fun getCast(id: Int): List<Actor>
    suspend fun getSimilarMoviesById(id: Int): List<Movie>
    suspend fun getReviews(id: Int): List<Review>
    suspend fun getMoviesByCategory(category: Genre): List<Movie>
}