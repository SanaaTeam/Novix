package details.usecase

import details.repository.MovieRepository

class GetMovieImagesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(id: Int): List<String> = movieRepository.getImages(id)

}