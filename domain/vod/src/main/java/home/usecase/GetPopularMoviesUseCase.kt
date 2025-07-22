package home.usecase

import details.repository.MovieRepository
import entity.Movie

class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return movieRepository.getPopularMovies()
    }
}
