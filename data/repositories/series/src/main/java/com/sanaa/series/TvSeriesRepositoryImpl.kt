package com.sanaa.series

import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import com.sanaa.series.mapper.toEntity
import details.repository.TvSeriesRepository
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import exceptions.NotFoundException

class TvSeriesRepositoryImpl(private val remoteDataSource: RemoteTvSeriesDataSource) :
    TvSeriesRepository {
    override suspend fun getTvSeriesDetails(id: Int): TvSeries {
        try {
            val video = remoteDataSource.getTvSeriesVideos(id)
            return remoteDataSource.getTvSeries(id).toEntity(video)
        } catch (_: Exception) {
            throw NotFoundException("Tv Series not found")
        }
    }

    override suspend fun getTvSeriesReviews(id: Int): List<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeriesImages(id: Int): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeriesCast(id: Int): List<Actor> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeriesSeason(
        seriesId: Int,
        seasonNumber: Int
    ): Season {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodeDetails(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodeImages(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<Actor> {
        TODO("Not yet implemented")
    }

}