package details.usecase.movie

import details.repository.MovieRepository
import entity.Movie

class GetSimilarMoviesByMovieId(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): List<Movie> = movieRepository.getSimilarMoviesByMovieId(movieId)
}