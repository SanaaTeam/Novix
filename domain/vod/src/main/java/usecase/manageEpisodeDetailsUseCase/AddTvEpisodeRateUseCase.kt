package usecase.manageEpisodeDetailsUseCase

import repository.TvShowRepository
import javax.inject.Inject

class AddTvEpisodeRateUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean {
        return tvShowRepository.addTvEpisodeRate(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            rating = rating
        )
    }
}