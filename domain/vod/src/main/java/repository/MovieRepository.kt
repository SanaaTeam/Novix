package repository

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import entity.WatchlistInfo

interface MovieRepository {
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun getImageUrls(id: Int, count: Int): List<String>
    suspend fun getMovieCast(id: Int): List<Actor>
    suspend fun getSimilarMoviesByMovieId(id: Int, page: Int): List<Movie>
    suspend fun getReviewsByMovieId(id: Int, page: Int): List<Review>
    suspend fun getMoviesByCategory(genreId: Int, page: Int): List<Movie>
    suspend fun getMovieTrailer(id: Int): String?
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun getTopRatedMovies(page: Int, genreId: Int?): List<Movie>
    suspend fun getUpcomingMovies(page: Int, genreId: Int?): List<Movie>
    suspend fun getTrendingMovies(page: Int, genreId: Int?): List<Movie>
    suspend fun getWatchlistMovies(
        page: Int,
        accountId: String,
        authorization: String,
    ): List<Movie>

    suspend fun addToWatchlist(
        accountId: String,
        authorization: String,
        body: WatchlistInfo
    )

    suspend fun getMovieGenres(): List<Genre>
}