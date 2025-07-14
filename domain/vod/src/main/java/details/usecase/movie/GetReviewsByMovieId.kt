package details.usecase.movie

import details.repository.MovieRepository
import entity.Review

class GetReviewsByMovieId(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): List<Review> = movieRepository.getReviewsByMovieId(movieId)
}