package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.mapper.actor.toDomain
import com.sanaa.vod.mapper.media.toDomain
import com.sanaa.vod.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import repository.TvSeriesRepository
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteTvShowDataSource
) : TvSeriesRepository {

    override suspend fun getTvSeriesDetails(id: Int): TvSeries = safeCall("Tv Series not found") {
        remoteDataSource.getTvShowDetails(id).toEntity()
    }

    override suspend fun getTvSeriesReviews(id: Int, page: Int): List<Review> = safeCall("Reviews not found") {
        remoteDataSource.getReviewsByTvShowId(id, page).map { it.toEntity() }
    }

    override suspend fun getTvSeriesImageUrls(id: Int, count: Int): List<String> =
        safeCall("Images not found") {
            remoteDataSource.getTvShowImageUrls(id).map { it.toEntity() }.take(count)
        }

    override suspend fun getTvSeriesByGenre(page: Int, genreId: Int): List<TvSeries> =
        safeCall("Tv Series not found") {
            remoteDataSource.getTvShowsByGenre(page, genreId).map { it.toEntity() }
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

    override suspend fun getTopRatedTvSeries(page: Int, genreId: Int?): List<TvSeries> =
        safeCall("Failed to fetch TvSeries TopRated") {
            remoteDataSource.fetchTrendingTvShows(page, genreId).map { it.toEntity() }
        }


    override suspend fun getTrendingTvSeries(page: Int, genreId: Int?): List<TvSeries> =
        safeCall("Failed to fetch TvSeries Trending") {
            remoteDataSource.fetchTopRatedTvShows(page, genreId).map { it.toEntity() }
        }

    override suspend fun getPopularSeries(page: Int): List<TvSeries> =
        safeCall("Failed to fetch TvSeries Popular") {
            remoteDataSource.fetchPopularTvShows(page).map { it.toEntity() }
        }

    override suspend fun getSeriesGenres(): List<Genre> {
        return safeCall("Genres not found") {
            remoteDataSource.getTvShowGenres().map { it.toEntity() }
        }
    }
}