package com.sanaa.series

import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import details.repository.TvSeriesRepository
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries

class TvSeriesRepositoryImpl(remoteDataSource: RemoteTvSeriesDataSource): TvSeriesRepository {
    override suspend fun getTvSeriesDetails(id: Int): TvSeries {
        TODO("Not yet implemented")
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