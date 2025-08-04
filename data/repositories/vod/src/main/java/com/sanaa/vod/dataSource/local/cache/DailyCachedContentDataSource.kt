package com.sanaa.vod.dataSource.local.cache

import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType

interface DailyCachedContentDataSource {
    suspend fun cachePopularMedia(media: List<CachedContentLocalDto>)
    suspend fun getCachedPopularMedia(mediaType: MediaType): List<CachedContentLocalDto>
    suspend fun cacheTopRatedMedia(media: List<CachedContentLocalDto>)
    suspend fun getCachedTopRatedMedia(mediaType: MediaType): List<CachedContentLocalDto>
    suspend fun cacheUpcomingMedia(media: List<CachedContentLocalDto>)
    suspend fun getCachedUpcomingMedia(mediaType: MediaType): List<CachedContentLocalDto>

    suspend fun clearExpiredCachedContent()
}