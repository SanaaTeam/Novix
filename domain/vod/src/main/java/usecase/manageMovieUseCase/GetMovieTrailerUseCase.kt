package usecase.manageMovieUseCase

import repository.MovieRepository
import javax.inject.Inject

class GetMovieTrailerUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int): String? =
        movieRepository.getMovieTrailer(id)
}