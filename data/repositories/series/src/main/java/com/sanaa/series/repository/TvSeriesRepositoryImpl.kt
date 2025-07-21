package com.sanaa.series.repository

import com.sanaa.series.dataSource.remote.RemoteTvSeriesDataSource
import com.sanaa.series.mapper.toDtoId
import com.sanaa.series.mapper.toEntity
import details.repository.TvSeriesRepository
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import java.net.UnknownHostException

class TvSeriesRepositoryImpl(private val remoteDataSource: RemoteTvSeriesDataSource) :
    TvSeriesRepository {

    override suspend fun getTvSeriesDetails(id: Int): TvSeries =
        safeCall("Tv Series not found") {
            remoteDataSource.getTvSeries(id).toEntity()
        }

    override suspend fun getTvSeriesReviews(id: Int): List<Review> =
        safeCall("Reviews not found") {
            remoteDataSource.getTvSeriesReviews(id).map { it.toEntity() }
        }

    override suspend fun getTvSeriesImages(id: Int, count: Int): List<String> =
        safeCall("Images not found") {
            remoteDataSource.getTvSeriesImages(id).map { it.toEntity() }.take(count)
        }

    override suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> =
        safeCall("Tv Series not found") {
            remoteDataSource.getTvSeriesByGenre(genre.toDtoId()).map { it.toEntity() }
        }

    override suspend fun getTvSeriesCast(id: Int): List<Actor> =
        safeCall("Cast not found") {
            remoteDataSource.getTvSeriesCast(id).map { it.toEntity() }
        }

    override suspend fun getTvSeriesSeason(seriesId: Int, seasonNumber: Int): Season =
        safeCall("Season not found") {
            remoteDataSource.getTvSeriesSeasonDetails(seriesId, seasonNumber).toEntity()
        }

    override suspend fun getEpisodeDetails(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode =
        safeCall("Episode not found") {
            remoteDataSource.getEpisodeDetails(seriesId, seasonNumber, episodeNumber).toEntity()
        }

    override suspend fun getEpisodeImages(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        count: Int
    ): List<String> = safeCall("Images not found") {
        remoteDataSource.getEpisodeImages(seriesId, seasonNumber, episodeNumber)
            .map { it.toEntity() }.take(count)
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<Actor> = safeCall("Guests not found") {
        remoteDataSource.getEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber)
            .map { it.toEntity() }
    }

    override suspend fun getTvSeriesTrailer(id: Int): String? =
        safeCall(errorMessage = "Trailer not found") {
            val videos = remoteDataSource.getTvSeriesVideos(id)
            videos.firstOrNull()?.toEntity()
        }


    private inline fun <T> safeCall(errorMessage: String, block: () -> T): T {
        try {
            return block()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("$errorMessage: ${e.message}")
        }
    }

}
