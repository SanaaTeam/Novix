package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.mapper.actor.toDomain
import com.sanaa.vod.mapper.media.toDomain
import com.sanaa.vod.mapper.media.toDtoId
import com.sanaa.vod.mapper.media.toEntity
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

class TvShowRepositoryImpl(
    private val remoteDataSource: RemoteTvShowDataSource
) : TvSeriesRepository {

    override suspend fun getTvSeriesDetails(id: Int): TvSeries = safeCall("Tv Series not found") {
        remoteDataSource.getTvShowDetails(id).toEntity()
    }

    override suspend fun getTvSeriesReviews(id: Int): List<Review> = safeCall("Reviews not found") {
        remoteDataSource.getReviewsByTvShowId(id).map { it.toEntity() }
    }

    override suspend fun getTvSeriesImageUrls(id: Int, count: Int): List<String> =
        safeCall("Images not found") {
            remoteDataSource.getTvShowImageUrls(id).map { it.toEntity() }.take(count)
        }

    override suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> =
        safeCall("Tv Series not found") {
            remoteDataSource.getTvShowsByGenre(genre.toDtoId()).map { it.toEntity() }
        }

    override suspend fun getTvSeriesCast(id: Int): List<Actor> = safeCall("Cast not found") {
        remoteDataSource.getTvShowCast(id).map { it.toDomain() }
    }

    override suspend fun getTvSeriesSeason(seriesId: Int, seasonNumber: Int): Season =
        safeCall("Season not found") {
            remoteDataSource.getTvShowSeasonDetails(seriesId, seasonNumber).toEntity()
        }

    override suspend fun getEpisodeDetails(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): Episode = safeCall("Episode not found") {
        remoteDataSource.getEpisodeDetails(seriesId, seasonNumber, episodeNumber).toEntity()
    }

    override suspend fun getEpisodeImageUrls(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int, count: Int
    ): List<String> = safeCall("Images not found") {
        remoteDataSource.getEpisodeImageUrls(seriesId, seasonNumber, episodeNumber)
            .map { it.toEntity() }.take(count)
    }

    override suspend fun getEpisodeGuestsOfHonor(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor> = safeCall("Guests not found") {
        remoteDataSource.getEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber)
            .map { it.toDomain() }
    }

    override suspend fun getTvSeriesTrailer(id: Int): String? =
        safeCall(errorMessage = "Trailer not found") {
            remoteDataSource.getTvShowVideosUrls(id).toDomain()
        }

    override suspend fun getTopRatedTvSeries(): List<TvSeries> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrendingTvSeries(): List<TvSeries> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeriesByGenre(genre: Genre): List<TvSeries> {
        TODO("Not yet implemented")
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