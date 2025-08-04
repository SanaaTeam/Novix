package com.sanaa.vod.cache

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.dataSource.local.cache.DailyCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category

class DailyCachedContentDataSourceImpl(
    private val cachedContentDao: CachedContentDao,
    private val cachedContentMetadataDao: CachedContentMetadataDao,
    private val languageProvider: LanguageProvider
) : DailyCachedContentDataSource {

    val language = languageProvider.getCurrentLanguage()

    override suspend fun cachePopularMedia(media: List<CachedContentLocalDto>) {
        val metadataId =
            cachedContentMetadataDao.insertCachedContentMetadata(
                CachedContentMetadataLocalDto(
                    category = Category.POPULAR.name,
                    language = language,
                )
            )
        cachedContentDao.insertAll(media.map { it.copy(metadataId = metadataId) })
    }

    override suspend fun getCachedPopularMedia(mediaType: MediaType): List<CachedContentLocalDto> {
        val cachedMetadata =
            cachedContentMetadataDao.getCachedContentMetadata(Category.POPULAR.name, language)
        return cachedContentDao.getMedia(metadataId = cachedMetadata.id, mediaType = mediaType.name)
    }

    override suspend fun cacheTopRatedMedia(media: List<CachedContentLocalDto>) {
        val metadataId =
            cachedContentMetadataDao.insertCachedContentMetadata(
                CachedContentMetadataLocalDto(
                    category = Category.TOP_RATED.name,
                    language = language,
                )
            )
        cachedContentDao.insertAll(media.map { it.copy(metadataId = metadataId) })
    }

    override suspend fun getCachedTopRatedMedia(mediaType: MediaType): List<CachedContentLocalDto> {
        val cachedMetadata =
            cachedContentMetadataDao.getCachedContentMetadata(Category.TOP_RATED.name, language)
        return cachedContentDao.getMedia(metadataId = cachedMetadata.id, mediaType = mediaType.name)
    }

    override suspend fun cacheUpcomingMedia(media: List<CachedContentLocalDto>) {
        val metadataId =
            cachedContentMetadataDao.insertCachedContentMetadata(
                CachedContentMetadataLocalDto(
                    category = Category.UPCOMING.name,
                    language = language,
                )
            )
        cachedContentDao.insertAll(media.map { it.copy(metadataId = metadataId) })
    }

    override suspend fun getCachedUpcomingMedia(mediaType: MediaType): List<CachedContentLocalDto> {
        val cachedMetadata =
            cachedContentMetadataDao.getCachedContentMetadata(Category.UPCOMING.name, language)
        return cachedContentDao.getMedia(metadataId = cachedMetadata.id, mediaType = mediaType.name)
    }

    override suspend fun clearExpiredCachedContent() {
        val oneDayAgo = System.currentTimeMillis() - (1000 * 60 * 60 * 24)
        cachedContentMetadataDao.clearExpiredMetadata(oneDayAgo)
    }
}