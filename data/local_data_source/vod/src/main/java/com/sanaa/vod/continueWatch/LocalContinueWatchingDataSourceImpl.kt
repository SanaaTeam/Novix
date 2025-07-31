package com.sanaa.vod.continueWatch

import com.sanaa.vod.continueWatch.dao.ContinueWatchingDao
import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingWithDetailsDto
import com.sanaa.vod.search.search_result.dao.MovieDao
import com.sanaa.vod.search.search_result.dao.SeriesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class LocalContinueWatchingDataSourceImpl(
    private val continueWatchingDao: ContinueWatchingDao,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao
) : LocalContinueWatchingDataSource {

    override suspend fun addOrUpdateMedia(continueWatchingDto: ContinueWatchingLocalDto) {
        continueWatchingDao.insertOrUpdateMedia(continueWatchingDto)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getContinueWatchingList(
        username: String,
        limit: Int
    ): Flow<List<ContinueWatchingWithDetailsDto>> {
        return continueWatchingDao.getContinueWatchingList(username, limit)
            .flatMapLatest { dtoList ->
                if (dtoList.isEmpty()) {
                    return@flatMapLatest flowOf(emptyList())
                }
                flow {
                    val itemsWithDetails = coroutineScope {
                        dtoList.map { dto ->
                            async {
                                val movie =
                                    if (dto.mediaType == "MOVIE") movieDao.getMovieById(dto.mediaId) else null
                                val series =
                                    if (dto.mediaType == "TV_SERIES") seriesDao.getSeriesById(dto.mediaId) else null

                                if (movie != null || series != null) {
                                    ContinueWatchingWithDetailsDto(
                                        continueWatchingDto = dto,
                                        movieDetails = movie,
                                        seriesDetails = series
                                    )
                                } else {
                                    null
                                }
                            }
                        }.awaitAll().filterNotNull()
                    }
                    emit(itemsWithDetails)
                }
            }
    }
}