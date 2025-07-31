package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import com.sanaa.vod.mapper.search.toEntity
import entity.ContinuableMedia
import entity.ContinueWatchingItem
import entity.MediaType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.ContinueWatchingRepository

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
) : ContinueWatchingRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getContinueWatchingList(
        username: String,
        limit: Int
    ): Flow<List<ContinueWatchingItem>> {
        return localContinueWatchingDataSource.getContinueWatchingList(username, limit)
            .map { listWithDetails ->
                listWithDetails.mapNotNull { detailsDto ->
                    val media = detailsDto.movieDetails?.let {
                        ContinuableMedia.MovieItem(it.toEntity())
                    } ?: detailsDto.seriesDetails?.let {
                        ContinuableMedia.TvSeriesItem(it.toEntity())
                    }

                    media?.let {
                        ContinueWatchingItem(
                            media = it,
                            episodeId = detailsDto.continueWatchingDto.episodeId
                        )
                    }
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