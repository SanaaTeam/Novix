package com.sanaa.series

import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import com.sanaa.series.mapper.toDtoId
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
            return remoteDataSource.getTvSeries(id).toEntity()
        } catch (_: Exception) {
            throw NotFoundException("Tv Series not found")
        }
    }

    override suspend fun getTvSeriesReviews(id: Int): List<Review> {
        try {
            val reviews = remoteDataSource.getTvSeriesReviews(id)
            return reviews.map { it.toEntity() }
        } catch (e: Exception) {
            throw NotFoundException("Reviews not found")
        }
    }

    override suspend fun getTvSeriesImages(id: Int): List<String> {
        try {
            return remoteDataSource.getTvSeriesImages(id).map { it.toEntity() }
        } catch (_: Exception) {
            throw NotFoundException("Images not found")
        }
    }

    override suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> {
        try {
            val tvSeries = remoteDataSource.getTvSeriesByGenre(genre.toDtoId())
            return tvSeries.map { it.toEntity() }
        } catch (_: Exception) {
            throw NotFoundException("Tv Series not found")
        }
    }

    override suspend fun getTvSeriesCast(id: Int): List<Actor> {
        try {
            return remoteDataSource.getTvSeriesCast(id).map { it.toEntity() }
        } catch (_: Exception) {
            throw NotFoundException("Cast not found")
        }
    }

    override suspend fun getTvSeriesSeason(
        seriesId: Int, seasonNumber: Int
    ): Season {
        try {
            return remoteDataSource.getTvSeriesSeasonDetails(seriesId, seasonNumber).toEntity()
        } catch (_: Exception) {
            throw NotFoundException("Season not found")
        }
    }

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): Episode {
        try {
            return remoteDataSource.getEpisodeDetails(seriesId, seasonNumber, episodeNumber)
                .toEntity()
        } catch (_: Exception) {
            throw NotFoundException("Episode not found")
        }
    }

    override suspend fun getEpisodeImages(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<String> {
        try {
            return remoteDataSource.getEpisodeImages(seriesId, seasonNumber, episodeNumber)
                .map { it.toEntity() }
        } catch (_: Exception) {
            throw NotFoundException("Images not found")
        }
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor> {
        try {
            return remoteDataSource.getEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber)
                .map { it.toEntity() }
        } catch (_: Exception) {
            throw NotFoundException("Guests not found")
        }
    }

    override suspend fun getTvSeriesTrailer(id: Int): String? {
        try {
            val videos = remoteDataSource.getTvSeriesVideos(id)
            return videos.firstOrNull()?.toEntity()
        } catch (_: Exception) {
            throw NotFoundException("Trailer not found")
        }
    }
}
