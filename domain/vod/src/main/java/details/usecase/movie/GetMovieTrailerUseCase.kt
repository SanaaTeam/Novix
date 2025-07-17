package details.usecase.movie

import details.repository.MovieRepository


class GetMovieTrailerUseCase(private val repository: MovieRepository) {
    suspend fun execute(id: Int): String? = repository.getMovieTrailer(id)
}