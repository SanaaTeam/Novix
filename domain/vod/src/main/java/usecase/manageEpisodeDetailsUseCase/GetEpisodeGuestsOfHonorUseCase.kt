package usecase.manageEpisodeDetailsUseCase

import entity.Actor
import repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeGuestsOfHonorUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<Actor> =
        tvShowRepository.getEpisodeGuestsOfHonor(tvShowId, seasonNumber, episodeNumber)
}