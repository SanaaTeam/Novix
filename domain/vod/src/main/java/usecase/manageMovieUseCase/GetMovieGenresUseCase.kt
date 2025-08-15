package usecase.manageMovieUseCase

import entity.Genre
import repository.MovieRepository
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Genre> {
        return movieRepository.getMovieGenres()
    }
}