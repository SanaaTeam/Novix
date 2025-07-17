package details.usecase.movie

import details.repository.MovieRepository

class GetMovieImagesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(id: Int): List<String> = movieRepository.getImagesUrls(id)
}