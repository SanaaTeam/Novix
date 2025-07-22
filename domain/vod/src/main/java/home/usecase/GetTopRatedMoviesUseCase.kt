package home.usecase

import details.repository.MovieRepository
import entity.Movie

class GetTopRatedMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return movieRepository.getTopRatedMovies()
    }
}
