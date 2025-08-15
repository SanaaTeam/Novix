package usecase.manageMovieUseCase

import entity.Movie
import repository.MovieRepository
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Int, page: Int): List<Movie> =
        movieRepository.getMoviesByCategory(genreId, page)
}