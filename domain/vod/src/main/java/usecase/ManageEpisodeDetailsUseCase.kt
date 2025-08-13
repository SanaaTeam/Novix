package usecase

import entity.Actor
import entity.Episode
import javax.inject.Inject
import repository.TvShowRepository

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
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean {
        return tvSeriesRepo.addTvEpisodeRate(
            tvShowId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            rating = rating
        )
    }
}