package details.usecase.movie

import details.repository.MovieRepository
import entity.Movie

class GetSimilarMovies(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): List<Movie> = movieRepository.getSimilarMovies(movieId)
}