package usecase

import entity.Actor
import entity.Episode
import repository.TvShowRepository
import javax.inject.Inject

class ManageEpisodeDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun getEpisodeDetails(id: Int, seasonNumber: Int, episodeNumber: Int): Episode =
        tvShowRepository.getEpisodeDetails(id, seasonNumber, episodeNumber)

    suspend fun getEpisodeGuestsOfHonor(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<Actor> =
        tvShowRepository.getEpisodeGuestsOfHonor(id, seasonNumber, episodeNumber)

    suspend fun getEpisodeImages(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        count: Int
    ): List<String> =
        tvShowRepository.getEpisodeImageUrls(id, seasonNumber, episodeNumber, count)

    suspend fun addTvEpisodeRate(
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