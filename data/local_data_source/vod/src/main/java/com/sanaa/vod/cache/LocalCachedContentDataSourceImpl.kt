package com.sanaa.vod.cache

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.cache.dao.MovieDao
import com.sanaa.vod.cache.dao.TvShowDao
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.util.TimeUtils
import javax.inject.Inject

class LocalCachedContentDataSourceImpl @Inject constructor(
    private val cachedContentDao: CachedContentDao,
    private val cachedContentMetadataDao: CachedContentMetadataDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val languageProvider: LanguageProvider
) : LocalCachedContentDataSource {

    private val currentLanguage: String
        get() = languageProvider.getCurrentLanguage()

    override suspend fun cacheMovie(movie: List<MovieLocalDto>, category: Category) {
        movieDao.insertAll(movie).also {
            movie.forEach { movie ->
                cacheContent(
                    mediaId = movie.id,
                    mediaType = MediaType.MOVIE,
                    category = category
                )
            }
        }
    }

    override suspend fun getCachedMovies(category: Category): List<MovieLocalDto> {
        val cachedMoviesInfo = getCachedContent(category, MediaType.MOVIE)

        if (cachedMoviesInfo.isEmpty()) {
            return emptyList()
        }

        val moviesIds = cachedMoviesInfo.map { it.mediaId }
        return movieDao.getMoviesByIds(moviesIds)
    }

    override suspend fun cacheTvShow(tvShow: List<TvShowLocalDto>, category: Category) {
        tvShowDao.insertAll(tvShow).also {
            tvShow.forEach { tvShow ->
                cacheContent(
                    mediaId = tvShow.id,
                    mediaType = MediaType.TV_SHOW,
                    category = category
                )
            }
        }
    }

    override suspend fun getCachedTvShows(category: Category): List<TvShowLocalDto> {
        val cachedTvShowsInfo = getCachedContent(category, MediaType.TV_SHOW)

        if (cachedTvShowsInfo.isEmpty()) {
            return emptyList()
        }

        val ids = cachedTvShowsInfo.map { it.mediaId }
        return tvShowDao.getTvShowsByIds(ids)
    }

    suspend fun getCachedContent(
        category: Category,
        mediaType: MediaType
    ): List<CachedContentLocalDto> {

        clearExpiredCache()

        val metadata = cachedContentMetadataDao.getCachedContentMetadata(
            category = category.name,
            language = currentLanguage
        )

        if (metadata == null || isExpired(metadata.timestamp)) {
            return emptyList()
        }

        return cachedContentDao.getCachedContentInfo(metadata.id, mediaType.name)
    }

    suspend fun cacheContent(
        mediaId: Int,
        mediaType: MediaType,
        category: Category
    ) {
        clearExpiredCache()

        val existingMetadata = cachedContentMetadataDao.getCachedContentMetadata(
            category = category.name,
            language = currentLanguage
        )

        val metadataId = existingMetadata?.id
            ?: cachedContentMetadataDao.insertCachedContentMetadata(
                CachedContentMetadataLocalDto(
                    language = currentLanguage,
                    category = category.name,
                )
            )

        clearExpiredCache()
        cachedContentDao.insert(
            CachedContentLocalDto(
                id = metadataId,
                mediaId = mediaId,
                mediaType = mediaType.name
            )
        )
    }

    private fun isExpired(timestamp: Long): Boolean {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        return currentTimestamp - timestamp > CACHE_EXPIRATION_TIME
    }

    override suspend fun clearExpiredCache() {
        val oneDayAgo = System.currentTimeMillis() - CACHE_EXPIRATION_TIME

        cachedContentMetadataDao.clearExpiredMetadata(oneDayAgo)

        // Clean up movies and tv shows that are no longer referenced
        movieDao.deleteUnreferencedMovies()
        tvShowDao.deleteUnreferencedTvShows()
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 1000 * 60 * 60 * 24L // 24 hours in milliseconds
    }
}