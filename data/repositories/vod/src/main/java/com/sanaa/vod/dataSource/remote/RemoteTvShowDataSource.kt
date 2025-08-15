package com.sanaa.vod.dataSource.remote

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.RatingResponse
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto

interface RemoteTvShowDataSource {
    suspend fun getTvShowDetails(id: Int): TvShowDto
    suspend fun getTvShowVideosUrls(id: Int): List<VideoDto>
    suspend fun getTvShowSeasonDetails(tvShowId: Int, seasonNumber: Int): SeasonDto
    suspend fun getTvShowImageUrls(id: Int): List<ImageDto>
    suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShowDto>
    suspend fun getReviewsByTvShowId(id: Int, page: Int): List<ReviewDto>
    suspend fun getTvShowCast(id: Int): List<ActorDto>
    suspend fun getEpisodeDetails(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeImageUrls(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<ImageDto>

    suspend fun getEpisodeGuestsOfHonor(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto>

    suspend fun getTvShowGenres(): List<GenreDto>
    suspend fun getTvShowRate(accountId: Long, sessionId: String): List<TvShowDto>
    suspend fun getEpisodesRate(accountId: Long, sessionId: String): List<EpisodeDto>

    suspend fun fetchPopularTvShows(page: Int): List<TvShowDto>
    suspend fun fetchTopRatedTvShows(page: Int, genreId: Int?): List<TvShowDto>
    suspend fun fetchTrendingTvShows(page: Int, genreId: Int?): List<TvShowDto>
    suspend fun sendTvShowRate(tvShowId: Int, sessionId: String, rating: Float): RatingResponse
    suspend fun sendTvEpisodeRate(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        sessionId: String,
        rating: Float
    ): RatingResponse
    suspend fun deleteTvShowRate(tvShowId: Int, sessionId: String): RatingResponse
}