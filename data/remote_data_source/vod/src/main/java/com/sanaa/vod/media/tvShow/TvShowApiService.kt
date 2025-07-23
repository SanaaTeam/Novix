package com.sanaa.vod.media.tvShow

import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.media.tvShow.response.GenreTvShowResponse
import com.sanaa.vod.media.tvShow.response.TvShowCastResponse
import com.sanaa.vod.media.tvShow.response.TvShowGuestOfStarsResponse
import com.sanaa.vod.media.tvShow.response.TvShowImagesResponse
import com.sanaa.vod.media.tvShow.response.TvShowReviewsResponse
import com.sanaa.vod.media.tvShow.response.TvShowVideosResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowApiService {
    @GET("tv/{tv_id}")
    suspend fun fetchTvShowsDetails(@Path("tv_id") id: Int): TvShowDto

    @GET("tv/{tv_id}/images")
    @Headers("Ignore-Language: true")
    suspend fun fetchTvShowsImages(@Path("tv_id") id: Int): TvShowImagesResponse

    @GET("discover/tv")
    suspend fun fetchTvShowsByCategory(@Query("with_genres") category: Int): GenreTvShowResponse

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/credits")
    suspend fun fetchEpisodeGuestsOfHonor(
        @Path("tv_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): TvShowGuestOfStarsResponse

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun fetchEpisodeDetails(
        @Path("tv_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): EpisodeDto

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun fetchSeasonDetails(
        @Path("tv_id") seriesId: Int, @Path("season_number") seasonNumber: Int
    ): SeasonDto

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/images")
    suspend fun fetchEpisodeImages(
        @Path("tv_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): TvShowImagesResponse

    @GET("tv/{tv_id}/videos")
    @Headers("Ignore-Language: true")
    suspend fun fetchTvShowsVideos(@Path("tv_id") id: Int): TvShowVideosResponse

    @GET("tv/{tv_id}/reviews")
    suspend fun fetchTvShowsReviews(@Path("tv_id") id: Int): TvShowReviewsResponse

    @GET("tv/{tv_id}/credits")
    suspend fun fetchTvShowsCast(@Path("tv_id") id: Int): TvShowCastResponse
}