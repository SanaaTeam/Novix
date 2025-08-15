package usecase.manageMovieUseCase

import entity.Movie
import repository.MovieRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int, genreId: Int?): List<Movie> =
        movieRepository.getTrendingMovies(page, genreId)
}