package usecase

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import repository.MovieRepository
import javax.inject.Inject

class ManageMovieUseCase @Inject constructor(
    private val movieRepo: MovieRepository
) {
    suspend fun getMovieDetails(id: Int): Movie =
        movieRepo.getMovieDetails(id)

    suspend fun getMovieCast(id: Int): List<Actor> =
        movieRepo.getMovieCast(id)

    suspend fun getMovieImages(id: Int): List<String> =
        movieRepo.getImageUrls(id, IMAGE_COUNT)

    suspend fun getMoviesByCategory(genreId: Int, page: Int): List<Movie> =
        movieRepo.getMoviesByCategory(genreId, page)

    suspend fun getMovieTrailer(id: Int): String? =
        movieRepo.getMovieTrailer(id)

    suspend fun getReviewsByMovieId(movieId: Int, page: Int): List<Review> =
        movieRepo.getReviewsByMovieId(movieId, page)

    suspend fun getSimilarMoviesByMovieId(movieId: Int, page: Int): List<Movie> =
        movieRepo.getSimilarMoviesByMovieId(movieId, page)

    suspend fun getPopularMovies(page: Int): List<Movie> =
        movieRepo.getPopularMovies(page)

    suspend fun getTopRatedMovies(page: Int, genreId: Int?): List<Movie> =
        movieRepo.getTopRatedMovies(page, genreId)

    suspend fun getTrendingMovies(page: Int, genreId: Int?): List<Movie> =
        movieRepo.getTrendingMovies(page, genreId)

    suspend fun getUpcomingMovies(page: Int, genreId: Int?): List<Movie> =
        movieRepo.getUpcomingMovies(page, genreId)

    suspend fun getMovieGenres(): List<Genre> {
        return movieRepo.getMovieGenres()
    }

    suspend fun getMovieRate(accountId: Long, movieId: Int): Int {
        return movieRepo.getMovieRate(accountId, movieId) ?: 0
    }

    suspend fun addMovieRate(movieId: Int, rating: Float): Boolean {
        return movieRepo.addMovieRate(movieId = movieId, rating = rating)
    }

    suspend fun getUserRatedMovies(): List<Movie> {
        return movieRepo.getUserRatedMovies()
    }

    suspend fun deleteMovieRate(movieId: Int): Boolean {
        return movieRepo.deleteMovieRate(movieId)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
