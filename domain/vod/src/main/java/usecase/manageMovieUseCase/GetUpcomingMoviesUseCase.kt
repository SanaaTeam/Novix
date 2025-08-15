package usecase.manageMovieUseCase

import entity.Movie
import repository.MovieRepository
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int, genreId: Int?): List<Movie> =
        movieRepository.getUpcomingMovies(page, genreId)
}