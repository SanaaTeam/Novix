package com.sanaa.vod.media.tvShow

import com.sanaa.vod.dataSource.remote.RemoteTvShowDataSource
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.RatingResponse
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import com.sanaa.vod.media.tvShow.request.TvShowRateRequest
import com.sanaa.vod.util.wrapApiCall
import javax.inject.Inject

class RemoteTvShowDataSourceImpl @Inject constructor(
    private val apiService: TvShowApiService,
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: Int): TvShowDto =
        wrapApiCall { apiService.fetchTvShowsDetails(id) }

    override suspend fun getTvShowVideosUrls(id: Int): List<VideoDto> = wrapApiCall {
        apiService.fetchTvShowsVideos(id).results
    }

    override suspend fun getTvShowSeasonDetails(
        tvShowId: Int, seasonNumber: Int
    ): SeasonDto = wrapApiCall {
        apiService.fetchSeasonDetails(tvShowId, seasonNumber)
    }

    override suspend fun getTvShowImageUrls(id: Int): List<ImageDto> = wrapApiCall {
        apiService.fetchTvShowsImages(id).backdrops
    }

    override suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShowDto> = wrapApiCall {
        apiService.fetchTvShowsByCategory(
            page = page,
            category = genreId
        ).results
    }

    override suspend fun getReviewsByTvShowId(id: Int, page: Int): List<ReviewDto> = wrapApiCall {
        apiService.fetchTvShowsReviews(id, page).results
    }

    override suspend fun getTvShowCast(id: Int): List<ActorDto> = wrapApiCall {
        apiService.fetchTvShowsCast(id).cast
    }

    override suspend fun getEpisodeDetails(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): EpisodeDto =
        wrapApiCall { apiService.fetchEpisodeDetails(tvShowId, seasonNumber, episodeNumber) }

    override suspend fun getEpisodeImageUrls(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ImageDto> = wrapApiCall {
        apiService.fetchEpisodeImages(tvShowId, seasonNumber, episodeNumber).backdrops
    }

    override suspend fun getEpisodeGuestsOfHonor(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto> = wrapApiCall {
        apiService.fetchEpisodeGuestsOfHonor(tvShowId, seasonNumber, episodeNumber).guestStars

    }

    override suspend fun getTvShowGenres(): List<GenreDto> {
        return apiService.fetchTvShowsGenres().genres
    }

    override suspend fun getTvShowRate(accountId: Long, sessionId: String): List<TvShowDto> =
        wrapApiCall {
            apiService.fetchTvShowRate(accountId = accountId, sessionId = sessionId).results
        }

    override suspend fun getEpisodesRate(accountId: Long, sessionId: String): List<EpisodeDto> =
        wrapApiCall {
            apiService.fetchEpisodesRate(sessionId = sessionId, accountId = accountId).results
        }

    override suspend fun fetchPopularTvShows(
        page: Int,
    ): List<TvShowDto> {
        return apiService.getPopularTvShows(page).results
    }

    override suspend fun fetchTopRatedTvShows(
        page: Int,
        genreId: Int?
    ): List<TvShowDto> {
        return apiService.fetchTopRatingTvShows(
            page,
            genreId?.toString()
        ).results
    }

    override suspend fun fetchTrendingTvShows(
        page: Int,
        genreId: Int?
    ): List<TvShowDto> {
        return apiService.fetchTrendingTvShows(
            page,
            genreId?.toString()
        ).results
    }

    override suspend fun fetchUpcomingTvShows(page: Int, genreId: Int?): List<TvShowDto> =
        wrapApiCall { apiService.fetchUpcomingTvShows(page, genreId?.toString()).results }

    override suspend fun sendTvShowRate(
        tvShowId: Int,
        sessionId: String,
        rating: Float
    ): RatingResponse {
        val response = apiService.rateTvShow(
            tvShowId = tvShowId,
            sessionId = sessionId,
            rating = TvShowRateRequest(value = rating)
        )
        return response
    }

    override suspend fun sendTvEpisodeRate(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        sessionId: String,
        rating: Float
    ): RatingResponse {
        val response = apiService.rateTvEpisode(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            sessionId = sessionId,
            rating = TvShowRateRequest(value = rating)
        )
        return response
    }
    override suspend fun deleteTvShowRate(tvShowId: Int, sessionId: String): RatingResponse =
        wrapApiCall {
            apiService.deleteTvShowRating(tvShowId, sessionId)
        }
}