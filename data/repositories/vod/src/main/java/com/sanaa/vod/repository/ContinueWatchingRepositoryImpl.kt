package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.mapper.search.toEntity
import entity.ContinuableMedia
import entity.ContinueWatchingItem
import entity.MediaType
import repository.ContinueWatchingRepository

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    private val localCacheDataSource: LocalCacheSearchDataSource
) : ContinueWatchingRepository {

    override suspend fun getContinueWatchingList(limit: Int): List<ContinueWatchingItem> {

        return localContinueWatchingDataSource.getContinueWatchingList(limit).mapNotNull { dto ->
            when (MediaType.valueOf(dto.mediaType)) {
                MediaType.MOVIE -> {
                    val movieDto = localCacheDataSource.getMoviesByQuery(
                        query = dto.mediaId.toString(),
                        limit = 1,
                        offset = 0
                    ).firstOrNull()

                    movieDto?.let {
                        ContinueWatchingItem(
                            media = ContinuableMedia.MovieItem(it.toEntity())
                        )
                    }
                }

                MediaType.TV_SERIES -> {
                    val seriesDto = localCacheDataSource.getTvSeriesByQuery(
                        query = dto.mediaId.toString(),
                        limit = 1,
                        offset = 0
                    ).firstOrNull()

                    seriesDto?.let {
                        ContinueWatchingItem(
                            media = ContinuableMedia.TvSeriesItem(it.toEntity()),
                            episodeId = dto.episodeId
                        )
                    }
                }
            }
        }
    }

    override suspend fun addItem(
        mediaId: Int,
        episodeId: Int?,
        mediaType: MediaType
    ) {
        val newItem = ContinueWatchingLocalDto(
            mediaId = mediaId,
            episodeId = episodeId,
            mediaType = mediaType.name
        )
        localContinueWatchingDataSource.addOrUpdateItem(newItem)
    }

    override suspend fun removeItem(mediaId: Int) {
        localContinueWatchingDataSource.deleteItem(mediaId)
    }
}