package details.repository

import entity.Episode
import entity.Guest
import entity.Review

interface EpisodeRepository {
    suspend fun getEpisodeDetails(id: Int): Episode
    suspend fun getEpisodeReviews(id: Int): List<Review>
    suspend fun getEpisodeImages(id: Int): List<String>
    suspend fun getGuestsOfHonor(id: Int): List<Guest>
}