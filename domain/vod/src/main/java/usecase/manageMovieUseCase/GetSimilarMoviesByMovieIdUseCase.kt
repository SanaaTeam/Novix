package usecase.manageMovieUseCase

import entity.Movie
import repository.MovieRepository
import javax.inject.Inject

class GetSimilarMoviesByMovieIdUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int): List<Movie> =
        movieRepository.getSimilarMoviesByMovieId(movieId, page)
}