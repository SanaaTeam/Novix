package com.sanaa.vod.cache

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.dataSource.local.cache.DailyCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import javax.inject.Inject

class DailyCachedContentDataSourceImpl @Inject constructor(
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
        clearExpiredCachedContent()
        cachedContentMetadataDao.getCachedContentMetadata(Category.POPULAR.name, language)?.let {
                return cachedContentDao.getMedia(metadataId = it.id, mediaType = mediaType.name)
            }
        return emptyList()
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
        clearExpiredCachedContent()
        cachedContentMetadataDao.getCachedContentMetadata(Category.TOP_RATED.name, language)?.let {
                return cachedContentDao.getMedia(metadataId = it.id, mediaType = mediaType.name)
            }
        return emptyList()
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
        clearExpiredCachedContent()
        cachedContentMetadataDao.getCachedContentMetadata(Category.UPCOMING.name, language)?.let {
            return cachedContentDao.getMedia(metadataId = it.id, mediaType = mediaType.name)
        }
        return emptyList()
    }

    override suspend fun clearExpiredCachedContent() {
        val oneDayAgo = System.currentTimeMillis() - (1000 * 60)
        cachedContentMetadataDao.clearExpiredMetadata(oneDayAgo)
    }
}