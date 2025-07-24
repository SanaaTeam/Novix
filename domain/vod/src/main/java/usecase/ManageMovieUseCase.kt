package usecase

import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import repository.MovieRepository

class ManageMovieUseCase(
    private val movieRepo: MovieRepository
) {
    suspend fun getMovieDetails(id: Int): Movie =
        movieRepo.getMovieDetails(id)

    suspend fun getMovieCast(id: Int): List<Actor> =
        movieRepo.getMovieCast(id)

    suspend fun getMovieImages(id: Int): List<String> =
        movieRepo.getImageUrls(id, IMAGE_COUNT)

    suspend fun getMoviesByCategory(genreId: Int): List<Movie> =
        movieRepo.getMoviesByCategory(genreId)

    suspend fun getMovieTrailer(id: Int): String? =
        movieRepo.getMovieTrailer(id)

    suspend fun getReviewsByMovieId(movieId: Int): List<Review> =
        movieRepo.getReviewsByMovieId(movieId)

    suspend fun getSimilarMoviesByMovieId(movieId: Int): List<Movie> =
        movieRepo.getSimilarMoviesByMovieId(movieId)

    suspend fun getPopularMovies(page: Int): List<Movie> =
        movieRepo.getPopularMovies(page)

    suspend fun getTopRatedMovies(page: Int, genreId: String?): List<Movie> =
        movieRepo.getTopRatedMovies(page, genreId)

    suspend fun getTrendingMovies(page: Int, genreId: String?): List<Movie> =
        movieRepo.getTrendingMovies(page, genreId)

    suspend fun getUpcomingMovies(page: Int, genreId: String?): List<Movie> =
        movieRepo.getUpcomingMovies(page, genreId)

    suspend fun getMovieGenres(): List<Genre> {
        return movieRepo.getMovieGenres()
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
