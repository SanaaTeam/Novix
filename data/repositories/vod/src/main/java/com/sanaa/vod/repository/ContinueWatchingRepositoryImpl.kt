package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.mapper.search.toEntity
import entity.ContinuableMedia
import entity.ContinueWatchingItem
import entity.MediaType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import repository.ContinueWatchingRepository

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    private val localCacheDataSource: LocalCacheSearchDataSource
) : ContinueWatchingRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingItem>> {
        return localContinueWatchingDataSource.getContinueWatchingList(username, limit)
            .flatMapLatest { media ->
                if (media.isEmpty()) {
                    return@flatMapLatest flowOf(emptyList())
                }
                flow {
                    val items = coroutineScope {
                        media.map { dto ->
                            async {
                                when (MediaType.valueOf(dto.mediaType)) {
                                    MediaType.MOVIE -> localCacheDataSource.getMoviesByQuery(dto.mediaId.toString(), 1, 0).firstOrNull()?.let {
                                        ContinueWatchingItem(media = ContinuableMedia.MovieItem(it.toEntity()))
                                    }
                                    MediaType.TV_SERIES -> localCacheDataSource.getTvSeriesByQuery(dto.mediaId.toString(), 1, 0).firstOrNull()?.let {
                                        ContinueWatchingItem(media = ContinuableMedia.TvSeriesItem(it.toEntity()), episodeId = dto.episodeId)
                                    }
                                }
                            }
                        }.awaitAll().filterNotNull()
                    }
                    emit(items)
                }
            }
    }

    override suspend fun addMedia(username: String, item: ContinueWatchingItem) {
        val mediaId = when (item.media) {
            is ContinuableMedia.MovieItem -> (item.media as ContinuableMedia.MovieItem).movie.id
            is ContinuableMedia.TvSeriesItem -> (item.media as ContinuableMedia.TvSeriesItem).series.id
        }
        val mediaType = when (item.media) {
            is ContinuableMedia.MovieItem -> MediaType.MOVIE
            is ContinuableMedia.TvSeriesItem -> MediaType.TV_SERIES
        }

        val newItemDto = ContinueWatchingLocalDto(
            mediaId = mediaId,
            episodeId = item.episodeId,
            mediaType = mediaType.name,
            username = username
        )
        localContinueWatchingDataSource.addOrUpdateMedia(newItemDto)
    }
}