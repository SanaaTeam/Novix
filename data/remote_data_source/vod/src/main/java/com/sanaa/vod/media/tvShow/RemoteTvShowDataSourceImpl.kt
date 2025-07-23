package com.sanaa.vod.media.tvShow

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource

class RemoteTvShowDataSourceImpl(
    private val apiService: TvShowApiService,
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: Int): TvShowDto = apiService.fetchTvShowsDetails(id)

    override suspend fun getTvShowVideosUrls(id: Int): List<VideoDto> =
        apiService.fetchTvShowsVideos(id).results

    override suspend fun getTvShowSeasonDetails(
        seriesId: Int, seasonNumber: Int
    ): SeasonDto = apiService.fetchSeasonDetails(seriesId, seasonNumber)

    override suspend fun getTvShowImageUrls(id: Int): List<ImageDto> =
        apiService.fetchTvShowsImages(id).backdrops

    override suspend fun getTvShowsByGenre(genreId: Int): List<TvShowDto> =
        apiService.fetchTvShowsByCategory(genreId).results

    override suspend fun getReviewsByTvShowId(id: Int): List<ReviewDto> =
        apiService.fetchTvShowsReviews(id).results

    override suspend fun getTvShowCast(id: Int): List<ActorDto> =
        apiService.fetchTvShowsCast(id).cast

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): EpisodeDto = apiService.fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber)

    override suspend fun getEpisodeImageUrls(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ImageDto> =
        apiService.fetchEpisodeImages(seriesId, seasonNumber, episodeNumber).backdrops

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto> =
        apiService.fetchEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber).guestStars

    override suspend fun getTvShowGenres(): List<GenreDto> {
        return apiService.fetchTvShowsGenres().genres
    }
}