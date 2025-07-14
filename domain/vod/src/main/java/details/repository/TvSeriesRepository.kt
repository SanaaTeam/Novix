package details.repository

import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries

interface TvSeriesRepository {
    suspend fun getTvSeriesDetails(id: Int): TvSeries
    suspend fun getTvSeriesReviews(id: Int): List<Review>
    suspend fun getTvSeriesImages(id: Int): List<String>
    suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries>
    suspend fun getTvSeriesCast(id: Int): List<Actor>
    suspend fun getTvSeriesSeason(seriesId: Int, seasonNumber: Int): List<Season>
    suspend fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int): Episode
    suspend fun getEpisodeImages(seriesId: Int, seasonNumber: Int, episodeNumber: Int): List<String>
    suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor>
}