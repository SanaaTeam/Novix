package details.usecase

import details.repository.TvSeriesRepository
import entity.Actor
import entity.Episode

class ManageEpisodeDetailsUseCase(
    private val tvSeriesRepo: TvSeriesRepository
) {
    suspend fun getEpisodeDetails(id: Int, seasonNumber: Int, episodeNumber: Int): Episode =
        tvSeriesRepo.getEpisodeDetails(id, seasonNumber, episodeNumber)

    suspend fun getEpisodeGuestsOfHonor(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<Actor> =
        tvSeriesRepo.getEpisodeGuestsOfHonor(id, seasonNumber, episodeNumber)

    suspend fun getEpisodeImages(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        count: Int
    ): List<String> =
        tvSeriesRepo.getEpisodeImages(id, seasonNumber, episodeNumber, count)
}
