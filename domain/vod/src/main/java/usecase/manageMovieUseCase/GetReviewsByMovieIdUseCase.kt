package usecase.manageMovieUseCase

import entity.Review
import repository.MovieRepository
import javax.inject.Inject

class GetReviewsByMovieIdUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int): List<Review> =
        movieRepository.getReviewsByMovieId(movieId, page)
}