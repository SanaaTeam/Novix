package usecase.manageMovieUseCase

import repository.MovieRepository
import javax.inject.Inject

class AddMovieRateUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, rating: Float): Boolean {
        return movieRepository.addMovieRate(movieId = movieId, rating = rating)
    }
}