package com.sanaa.vod.dataSource.remote.tvShow

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto


interface RemoteTvSeriesDataSource {
    suspend fun getTvSeries(id: Int): TvShowDto
    suspend fun getTvSeriesVideos(id: Int): List<VideoDto>
    suspend fun getTvSeriesSeasonDetails(seriesId: Int, seasonNumber: Int): SeasonDto
    suspend fun getTvSeriesImages(id: Int): List<ImageDto>
    suspend fun getTvSeriesByGenre(genreId: Int): List<TvShowDto>
    suspend fun getTvSeriesReviews(id: Int): List<ReviewDto>
    suspend fun getTvSeriesCast(id: Int): List<ActorDto>
    suspend fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeImages(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<ImageDto>

    suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto>
}