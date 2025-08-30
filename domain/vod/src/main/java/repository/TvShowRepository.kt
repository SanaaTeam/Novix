package repository

import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowDetails(id: Int): TvShow
    suspend fun getTvShowReviews(id: Int, page: Int): List<Review>
    suspend fun getTvShowImageUrls(id: Int, count: Int): List<String>
    suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShow>
    suspend fun getTvShowCast(id: Int): List<Actor>
    suspend fun getTvShowSeason(tvShowId: Int, seasonNumber: Int): Season
    suspend fun getEpisodeDetails(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): Episode
    suspend fun getEpisodeImageUrls(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int, count: Int
    ): List<String>

    suspend fun getEpisodeGuestsOfHonor(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor>

    suspend fun getTvShowTrailer(id: Int): String?
    suspend fun getTopRatedTvShows(page: Int, genreId: Int?): List<TvShow>
    suspend fun getTrendingTvShows(page: Int, genreId: Int?): List<TvShow>
    suspend fun getPopularTvShows(page: Int): List<TvShow>
    suspend fun getTvShowGenres(refreshCash:Boolean = false): List<Genre>
    suspend fun getTvShowRate(accountId: Long, tvShowId: Int): Int?
    suspend fun getEpisodesRate(accountId: Long, seasonNumber: Int, episodeNumber: Int): Int?
    suspend fun addTvShowRate(tvShowId: Int, rating: Float): Boolean
    suspend fun addTvEpisodeRate(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean
    suspend fun getRatedTvShows(accountId: Long, sessionId: String): List<TvShow>
    suspend fun deleteTvShowRate(tvShowId: Int): Boolean

    suspend fun addTvShowToFavorite(tvShowId: Int): Boolean
    suspend fun fetchFavoriteTvShows(page: Int): List<TvShow>
    suspend fun deleteTvShowFromFavorite(tvShowId: Int): Boolean
}