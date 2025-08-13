package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.remote.RemoteTvShowDataSource
import com.sanaa.vod.repository.mapper.cachedContent.toEntity
import com.sanaa.vod.repository.mapper.cachedContent.toLocalDto
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvShow
import kotlinx.coroutines.flow.first
import repository.TvShowRepository
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteTvShowDataSource,
    private val localCachedContentDataSource: LocalCachedContentDataSource,
    private val preferences: PreferencesManager
) : TvShowRepository {

    override suspend fun getTvShowDetails(id: Int): TvShow = safeCall("Tv Series not found") {
        remoteDataSource.getTvShowDetails(id).toEntity()
    }

    override suspend fun getTvShowReviews(id: Int, page: Int): List<Review> =
        safeCall("Reviews not found") {
            remoteDataSource.getReviewsByTvShowId(id, page).map { it.toEntity() }
        }

    override suspend fun getTvShowImageUrls(id: Int, count: Int): List<String> =
        safeCall("Images not found") {
            remoteDataSource.getTvShowImageUrls(id).map { it.toEntity() }.take(count)
        }

    override suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShow> =
        safeCall("Tv Series not found") {
            remoteDataSource.getTvShowsByGenre(page, genreId).map { it.toEntity() }
        }

    override suspend fun getTvShowCast(id: Int): List<Actor> = safeCall("Cast not found") {
        remoteDataSource.getTvShowCast(id).map { it.toEntity() }
    }

    override suspend fun getTvShowSeason(tvShowId: Int, seasonNumber: Int): Season =
        safeCall("Season not found") {
            remoteDataSource.getTvShowSeasonDetails(tvShowId, seasonNumber).toEntity()
        }

    override suspend fun getEpisodeDetails(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): Episode = safeCall("Episode not found") {
        remoteDataSource.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber).toEntity()
    }

    override suspend fun getEpisodeImageUrls(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int, count: Int
    ): List<String> = safeCall("Images not found") {
        remoteDataSource.getEpisodeImageUrls(tvShowId, seasonNumber, episodeNumber)
            .map { it.toEntity() }.take(count)
    }

    override suspend fun getEpisodeGuestsOfHonor(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): List<Actor> = safeCall("Guests not found") {
        remoteDataSource.getEpisodeGuestsOfHonor(tvShowId, seasonNumber, episodeNumber)
            .map { it.toEntity() }
    }

    override suspend fun getTvShowTrailer(id: Int): String? =
        safeCall(errorMessage = "Trailer not found") {
            remoteDataSource.getTvShowVideosUrls(id).toEntity()
        }

    override suspend fun getTopRatedTvShows(page: Int, genreId: Int?): List<TvShow> =
        safeCall("Failed to fetch TvSeries TopRated") {
           if (page == 1 && genreId == null) {
                val cachedMovies =
                    localCachedContentDataSource.getCachedTvShows(category = Category.TOP_RATED_MEDIA)
                if (cachedMovies.isNotEmpty()) {
                    return cachedMovies.map { it.toEntity() }
                }

            }

            return remoteDataSource.fetchTopRatedTvShows(page, genreId).map { it.toEntity() }.also {
                if (page == 1 && genreId == null) {
                    localCachedContentDataSource.cacheTvShow(
                        tvShows = it.map { it.toLocalDto() },
                        category = Category.TOP_RATED_MEDIA
                    )
                }
            }
        }


    override suspend fun getTrendingTvShows(page: Int, genreId: Int?): List<TvShow> =
        safeCall("Failed to fetch TvSeries Trending") {
            remoteDataSource.fetchTrendingTvShows(page, genreId).map { it.toEntity() }
        }

    override suspend fun getPopularTvShows(page: Int): List<TvShow> =
        safeCall("Failed to fetch TvSeries Popular") {
            if (page == 1) {
                val cachedMovies =
                    localCachedContentDataSource.getCachedTvShows(category = Category.POPULAR_MEDIA)
                if (cachedMovies.isNotEmpty()) {
                    return cachedMovies.map { it.toEntity() }
                }
            }

            return remoteDataSource.fetchPopularTvShows(page).map { it.toEntity() }.also {
                if (page == 1) {
                    localCachedContentDataSource.cacheTvShow(
                        tvShows = it.map { it.toLocalDto() },
                        category = Category.POPULAR_MEDIA
                    )
                }
            }
        }

    override suspend fun getTvShowGenres(): List<Genre> {
        return safeCall("Genres not found") {
            remoteDataSource.getTvShowGenres().map { it.toEntity() }
            val cachedGenres = localCachedContentDataSource.getCachedGenres(category = Category.TV_SHOW_GENRES)
            if (cachedGenres.isNotEmpty()) {
                return cachedGenres.map { it.toEntity() }
            }

            return remoteDataSource.getTvShowGenres().map { it.toEntity() }.also {
                localCachedContentDataSource.cacheGenres(
                    genres = it.map { it.toLocalDto() },
                    category = Category.TV_SHOW_GENRES
                )
            }
        }
    }

    override suspend fun getTvShowRate(accountId: Long, tvShowId: Int): Int? {
        return safeCall("Failed to fetch TvSeries Rate") {
            val sessionId = preferences.sessionId.first()
            remoteDataSource.getTvShowRate(accountId, sessionId).map { it.toEntity() }
                .find { it.id == tvShowId }?.rating
        }
    }

    override suspend fun getEpisodesRate(
        accountId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Int? {
        return safeCall("Failed to fetch Episodes Rate") {
            val sessionId = preferences.sessionId.first()
            remoteDataSource.getEpisodesRate(accountId, sessionId).map { it.toEntity() }
                .find {
                    it.seasonNumber == seasonNumber && it.number == episodeNumber
                }?.rating
        }
    }

    override suspend fun addTvShowRate(tvShowId: Int, rating: Float): Boolean {
        return safeCall("Failed to add TV series rate") {
            val sessionId = preferences.sessionId.first()
            remoteDataSource.sendTvSeriesRate(
                seriesId = tvShowId,
                sessionId = sessionId,
                rating = rating
            ).isSuccess
        }
    }

    override suspend fun addTvEpisodeRate(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Float
    ): Boolean {
        return safeCall("Failed to add TV episode rate") {
            val sessionId = preferences.sessionId.first()
            remoteDataSource.sendTvEpisodeRate(
                seriesId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                sessionId = sessionId,
                rating = rating
            ).isSuccess
        }
    }

    override suspend fun getRatedTvShows(accountId: Long, sessionId: String): List<TvShow> {
        return safeCall("Failed to fetch user rated tv shows") {
            remoteDataSource.getTvShowRate(accountId, sessionId).map { it.toEntity() }
        }
    }

    override suspend fun deleteTvShowRate(tvShowId: Int): Boolean {
        return safeCall("Failed to delete tv series rate") {
            val sessionId = preferences.sessionId.first()
            remoteDataSource.deleteTvSeriesRate(tvShowId, sessionId).isSuccess
        }
    }
}