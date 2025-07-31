package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.mapper.search.toEntity
import entity.ContinuableMedia
import entity.ContinueWatchingItem
import entity.MediaType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import repository.ContinueWatchingRepository

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    private val localCacheDataSource: LocalCacheSearchDataSource
) : ContinueWatchingRepository {

    override suspend fun getContinueWatchingList(username: String, limit: Int): List<ContinueWatchingItem> = coroutineScope {
        val dtos = localContinueWatchingDataSource.getContinueWatchingList(username, limit)

        dtos.map { dto ->
            async {
                when (MediaType.valueOf(dto.mediaType)) {
                    MediaType.MOVIE -> {
                        localCacheDataSource.getMoviesByQuery(
                            query = dto.mediaId.toString(),
                            limit = 1,
                            offset = 0
                        ).firstOrNull()?.let { movieDto ->
                            ContinueWatchingItem(
                                media = ContinuableMedia.MovieItem(movieDto.toEntity())
                            )
                        }
                    }
                    MediaType.TV_SERIES -> {
                        localCacheDataSource.getTvSeriesByQuery(
                            query = dto.mediaId.toString(),
                            limit = 1,
                            offset = 0
                        ).firstOrNull()?.let { seriesDto ->
                            ContinueWatchingItem(
                                media = ContinuableMedia.TvSeriesItem(seriesDto.toEntity()),
                                episodeId = dto.episodeId
                            )
                        }
                    }
                }
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun addItem(
        username: String,
        mediaId: Int,
        episodeId: Int?,
        mediaType: MediaType
    ) {
        val newItem = ContinueWatchingLocalDto(
            mediaId = mediaId,
            episodeId = episodeId,
            mediaType = mediaType.name,
            username = username
        )
        localContinueWatchingDataSource.addOrUpdateItem(newItem)
    }

    override suspend fun removeItem(mediaId: Int, username: String) {
        localContinueWatchingDataSource.deleteItem(mediaId, username)
    }
}