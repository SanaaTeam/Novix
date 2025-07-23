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
    suspend fun getTvSeriesImageUrls(id: Int, count: Int): List<String>
    suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries>
    suspend fun getTvSeriesCast(id: Int): List<Actor>
    suspend fun getTvSeriesSeason(seriesId: Int, seasonNumber: Int): Season
    suspend fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int): Episode
    suspend fun getEpisodeImageUrls(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int, count: Int
    ): List<String>

    suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor>

    suspend fun getTvSeriesTrailer(id: Int): String?
    suspend fun getTopRatedTvSeries(): List<TvSeries>
    suspend fun getTrendingTvSeries(genre: Genre): List<TvSeries>
    suspend fun getPopularSeries(genre: Genre): List<TvSeries>
    suspend fun getSeriesGenres(): List<Genre>
}