package usecase.manageEpisodeDetailsUseCase

import repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeImagesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        count: Int
    ): List<String> =
        tvShowRepository.getEpisodeImageUrls(tvShowId, seasonNumber, episodeNumber, count)
}