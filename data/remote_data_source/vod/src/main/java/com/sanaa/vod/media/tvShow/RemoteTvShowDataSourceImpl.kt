package com.sanaa.vod.media.tvShow

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.RatingResponse
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
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
        seriesId: Int, seasonNumber: Int
    ): SeasonDto = wrapApiCall {
        apiService.fetchSeasonDetails(seriesId, seasonNumber)
    }

    override suspend fun getTvShowImageUrls(id: Int): List<ImageDto> = wrapApiCall {
        apiService.fetchTvShowsImages(id).backdrops
    }

    override suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShowDto> = wrapApiCall {
        apiService.fetchTvShowsByCategory(
            page = page,
            category = genreId
        ).results.distinctBy { it.id }
    }

    override suspend fun getReviewsByTvShowId(id: Int, page: Int): List<ReviewDto> = wrapApiCall {
        apiService.fetchTvShowsReviews(id, page).results.distinctBy { it.id }
    }

    override suspend fun getTvShowCast(id: Int): List<ActorDto> = wrapApiCall {
        apiService.fetchTvShowsCast(id).cast.distinctBy { it.id }
    }

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): EpisodeDto =
        wrapApiCall { apiService.fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber) }

    override suspend fun getEpisodeImageUrls(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ImageDto> = wrapApiCall {
        apiService.fetchEpisodeImages(seriesId, seasonNumber, episodeNumber).backdrops
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto> = wrapApiCall {
        apiService.fetchEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber).guestStars
            .distinctBy { it.id }
    }

    override suspend fun getTvShowGenres(): List<GenreDto> {
        return apiService.fetchTvShowsGenres().genres.distinctBy { it.id }
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
        return apiService.getPopularTvShows(page).results.distinctBy { it.id }
    }

    override suspend fun fetchTopRatedTvShows(
        page: Int,
        genreId: Int?
    ): List<TvShowDto> {
        return apiService.fetchTopRatingTvShows(
            page,
            genreId?.toString()
        ).results.distinctBy { it.id }
    }

    override suspend fun fetchTrendingTvShows(
        page: Int,
        genreId: Int?
    ): List<TvShowDto> {
        return apiService.fetchTrendingTvShows(
            page,
            genreId?.toString()
        ).results.distinctBy { it.id }
    }

    override suspend fun sendTvSeriesRate(
        seriesId: Int,
        sessionId: String,
        rating: Float
    ): RatingResponse {
        val response = apiService.rateTvSeries(
            seriesId = seriesId,
            sessionId = sessionId,
            rating = TvShowRateRequest(value = rating)
        )
        return response
    }

    override suspend fun sendTvEpisodeRate(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        sessionId: String,
        rating: Float
    ): RatingResponse {
        val response = apiService.rateTvEpisode(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            sessionId = sessionId,
            rating = TvShowRateRequest(value = rating)
        )
        return response
    }
    override suspend fun deleteTvSeriesRate(seriesId: Int, sessionId: String): RatingResponse =
        wrapApiCall {
            apiService.deleteTvShowRating(seriesId, sessionId)
        }
}