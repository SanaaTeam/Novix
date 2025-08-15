package usecase.manageMovieUseCase

import repository.MovieRepository
import javax.inject.Inject

class GetMovieImagesUrlUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int, count: Int = IMAGE_COUNT): List<String> =
        movieRepository.getImageUrls(id, count)

    private companion object {
        const val IMAGE_COUNT = 10
    }
}