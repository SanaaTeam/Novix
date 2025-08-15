package usecase.manageEpisodeDetailsUseCase


import entity.Episode
import repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode =
        tvShowRepository.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
}