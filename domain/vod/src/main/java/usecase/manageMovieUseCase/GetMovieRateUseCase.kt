package usecase.manageMovieUseCase

import repository.MovieRepository
import javax.inject.Inject

class GetMovieRateUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(accountId: Long, movieId: Int): Int {
        return movieRepository.getMovieRate(accountId, movieId) ?: 0
    }
}