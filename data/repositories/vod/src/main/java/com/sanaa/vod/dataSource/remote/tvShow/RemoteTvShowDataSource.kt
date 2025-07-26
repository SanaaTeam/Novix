package com.sanaa.vod.dataSource.remote.tvShow

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto


interface RemoteTvShowDataSource {
    suspend fun getTvShowDetails(id: Int): TvShowDto
    suspend fun getTvShowVideosUrls(id: Int): List<VideoDto>
    suspend fun getTvShowSeasonDetails(seriesId: Int, seasonNumber: Int): SeasonDto
    suspend fun getTvShowImageUrls(id: Int): List<ImageDto>
    suspend fun getTvShowsByGenre(genreId: Int): List<TvShowDto>
    suspend fun getReviewsByTvShowId(id: Int): List<ReviewDto>
    suspend fun getTvShowCast(id: Int): List<ActorDto>
    suspend fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeImageUrls(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<ImageDto>

    suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<ActorDto>

    suspend fun getTvShowGenres(): List<GenreDto>

    suspend fun fetchPopularTvShows(page: Int): List<TvShowDto>
    suspend fun fetchTopRatedTvShows(page: Int, genreId: Int?): List<TvShowDto>
    suspend fun fetchTrendingTvShows(page: Int, genreId: Int?): List<TvShowDto>
}