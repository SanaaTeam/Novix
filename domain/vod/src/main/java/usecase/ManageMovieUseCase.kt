package usecase

import repository.MovieRepository
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review

class ManageMovieUseCase(
    private val movieRepo: MovieRepository
) {
    suspend fun getMovieDetails(id: Int): Movie =
        movieRepo.getMovieDetails(id)

    suspend fun getMovieCast(id: Int): List<Actor> =
        movieRepo.getMovieCast(id)

    suspend fun getMovieImages(id: Int): List<String> =
        movieRepo.getImageUrls(id, IMAGE_COUNT)

    suspend fun getMoviesByCategory(category: Genre): List<Movie> =
        movieRepo.getMoviesByCategory(category)

    suspend fun getMovieTrailer(id: Int): String? =
        movieRepo.getMovieTrailer(id)

    suspend fun getReviewsByMovieId(movieId: Int): List<Review> =
        movieRepo.getReviewsByMovieId(movieId)

    suspend fun getSimilarMoviesByMovieId(movieId: Int): List<Movie> =
        movieRepo.getSimilarMoviesByMovieId(movieId)

    suspend fun getPopularMovies(): List<Movie> =
        movieRepo.getPopularMovies()

    suspend fun getTopRatedMovies(genre: Genre): List<Movie> =
        movieRepo.getTopRatedMovies(genre)

    suspend fun getTrendingMovies(genre: Genre): List<Movie> =
        movieRepo.getTrendingMovies(genre)

    suspend fun getUpcomingMovies(genre: Genre): List<Movie> =
        movieRepo.getUpcomingMovies(genre)

    suspend fun getMovieGenres(): List<Genre> {
        return movieRepo.getMovieGenres()
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
