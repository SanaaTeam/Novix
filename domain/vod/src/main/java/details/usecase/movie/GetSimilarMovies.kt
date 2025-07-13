package details.usecase.movie

import details.repository.MovieRepository
import entity.Movie

class GetSimilarMoviesById(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): List<Movie> = movieRepository.getSimilarMoviesById(movieId)
}