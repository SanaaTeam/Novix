package details.usecase.movie

import details.repository.MovieRepository
import entity.Genre
import entity.Movie

class GetMoviesByCategory(private val movieRepository: MovieRepository) {
    suspend fun execute(category: Genre): List<Movie> =
        movieRepository.getMoviesByCategory(category)
}