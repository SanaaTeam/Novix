package details.usecase.movie

import details.repository.MovieRepository
import entity.Movie

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(id: Int): Movie = movieRepository.getMovieDetails(id)
}