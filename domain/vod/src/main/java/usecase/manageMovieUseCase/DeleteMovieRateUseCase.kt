package usecase.manageMovieUseCase

import repository.MovieRepository
import javax.inject.Inject

class DeleteMovieRateUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        return movieRepository.deleteMovieRate(movieId)
    }
}