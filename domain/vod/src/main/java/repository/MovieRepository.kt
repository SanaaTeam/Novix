package repository

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review

interface MovieRepository {
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun getImageUrls(id: Int, count: Int): List<String>
    suspend fun getMovieCast(id: Int): List<Actor>
    suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie>
    suspend fun getReviewsByMovieId(id: Int): List<Review>
    suspend fun getMoviesByCategory(category: Genre): List<Movie>
    suspend fun getMovieTrailer(id: Int): String?
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun getTopRatedMovies(page: Int, genre: Genre?): List<Movie>
    suspend fun getUpcomingMovies(page: Int, genre: Genre?): List<Movie>
    suspend fun getTrendingMovies(page: Int, genre: Genre?): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
}