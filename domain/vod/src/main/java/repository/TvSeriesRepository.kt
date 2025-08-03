package repository

import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries

interface TvSeriesRepository {
    suspend fun getTvSeriesDetails(id: Int): TvSeries
    suspend fun getTvSeriesReviews(id: Int, page: Int): List<Review>
    suspend fun getTvSeriesImageUrls(id: Int, count: Int): List<String>
    suspend fun getTvSeriesByGenre(page: Int, genreId: Int): List<TvSeries>
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
    suspend fun getTopRatedTvSeries(page: Int, genreId: Int?): List<TvSeries>
    suspend fun getTrendingTvSeries(page: Int, genreId: Int?): List<TvSeries>
    suspend fun getPopularSeries(page: Int): List<TvSeries>
    suspend fun getSeriesGenres(): List<Genre>
    suspend fun getSeriesRate(accountId: Long, seriesId: Int): Int?
    suspend fun getEpisodesRate(accountId: Long, seasonNumber: Int, episodeNumber: Int): Int?
    suspend fun addTvSeriesRate(seriesId: Int, rating: Float): Boolean
    suspend fun addTvEpisodeRate(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean
    suspend fun getUserRatedTvSeries(userId: String): List<TvSeries>
    suspend fun deleteTvSeriesRate(seriesId: Int): Boolean
}