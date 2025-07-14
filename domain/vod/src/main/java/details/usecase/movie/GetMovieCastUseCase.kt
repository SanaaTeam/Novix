package details.usecase.movie

import details.repository.MovieRepository
import entity.Actor

class GetMovieCastUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(id: Int): List<Actor> = movieRepository.getCast(id)
}