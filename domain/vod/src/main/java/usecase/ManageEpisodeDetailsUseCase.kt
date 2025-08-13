package usecase

import entity.Actor
import entity.Episode
import repository.TvShowRepository
import javax.inject.Inject

class ManageEpisodeDetailsUseCase @Inject constructor(
    private val tvSeriesRepo: TvShowRepository
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
        tvSeriesRepo.getEpisodeImageUrls(id, seasonNumber, episodeNumber, count)

    suspend fun addTvEpisodeRate(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean {
        return tvSeriesRepo.addTvEpisodeRate(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            rating = rating
        )
    }
}