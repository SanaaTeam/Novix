package details.usecase

import details.repository.MovieRepository
import entity.Actor
import entity.Movie
import entity.Genre
import entity.Review

class ManageMovieDetailsUseCase(
    private val movieRepo: MovieRepository)
{
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
    private companion object {
        const val IMAGE_COUNT = 10
    }
}
