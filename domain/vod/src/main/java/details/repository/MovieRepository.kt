package details.repository

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun getImageUrls(id: Int, count: Int): List<String>
    suspend fun getMovieCast(id: Int): List<Actor>
    suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie>
    suspend fun getReviewsByMovieId(id: Int): List<Review>
    suspend fun getMoviesByCategory(category: Genre): List<Movie>
    suspend fun getMovieTrailer(id: Int): String?
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(): List<Movie>
    suspend fun getUpcomingMovies(): List<Movie>
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun getMoviesByGenre(genre: Genre): List<Movie>
}