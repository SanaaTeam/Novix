package usecase

import repository.TvSeriesRepository
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
        tvSeriesRepo.getEpisodeImageUrls(id, seasonNumber, episodeNumber, count)

    suspend fun addTvEpisodeRate(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ) {
        return tvSeriesRepo.addTvEpisodeRate(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            rating = rating
        )
    }
}
