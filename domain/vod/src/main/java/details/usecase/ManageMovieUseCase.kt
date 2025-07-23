package details.usecase

import details.repository.MovieRepository
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

    suspend fun getPopularMovies(): List<Movie> {
        return movieRepo.getPopularMovies()
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        return movieRepo.getTopRatedMovies()
    }

    suspend fun getTrendingMovies(): List<Movie> {
        return movieRepo.getTrendingMovies()
    }

    suspend fun getUpcomingMovies(): List<Movie> {
        return movieRepo.getUpcomingMovies()
    }

    suspend fun getMoviesByGenre(genre: Genre): List<Movie> {
        return movieRepo.getMoviesByGenre(genre)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
