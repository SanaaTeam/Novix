package com.sanaa.series.data_source.remote

import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.VideoDto

interface RemoteTvSeriesDataSource {
    suspend fun getTvSeries(id: Int): TvSeriesDto
    suspend fun getTvSeriesVideos(id: Int): List<VideoDto>
    suspend fun getTvSeriesSeasonDetails(seriesId: Int, seasonNumber: Int): SeasonDto
    suspend fun getTvSeriesImages(id: Int): List<String>
    suspend fun getTvSeriesByGenre(genreId: Int): List<TvSeriesDto>
    suspend fun getTvSeriesReviews(id: Int): List<ReviewDto>
    suspend fun getTvSeriesCast(id: Int): List<ActorDto>
    suspend fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeImages(seriesId: Int, seasonNumber: Int, episodeNumber: Int): List<String>
    suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto>
}