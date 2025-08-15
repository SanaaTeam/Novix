package usecase.manageMovieUseCase

import entity.Movie
import repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int): List<Movie> =
        movieRepository.getPopularMovies(page)
}