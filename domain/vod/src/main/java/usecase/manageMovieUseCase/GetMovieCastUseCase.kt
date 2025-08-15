package usecase.manageMovieUseCase

import entity.Actor
import repository.MovieRepository
import javax.inject.Inject

class GetMovieCastUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int): List<Actor> =
        movieRepository.getMovieCast(id)
}