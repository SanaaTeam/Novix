package com.sanaa.vod.history

import com.sanaa.vod.dataSource.local.history.LocalHistoryDataSource
import com.sanaa.vod.dataSource.local.history.dto.search.QueryLocalDto
import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.history.dao.QueryDao
import com.sanaa.vod.history.dao.RecentViewedDao
import com.sanaa.vod.history.dao.WatchedMediaHistoryDao
import kotlinx.coroutines.flow.Flow
import usecase.search.search_param.MediaType
import javax.inject.Inject

class LocalHistoryDataSourceImpl @Inject constructor(
    private val queryDao: QueryDao,
    private val recentViewedDao: RecentViewedDao,
    private val watchedMediaHistoryDao: WatchedMediaHistoryDao
) : LocalHistoryDataSource {
    override suspend fun insertQuery(query: String) {
        val normalizedQuery = query.trim().replace(Regex("\\s+"), " ")
        queryDao.upsertQuery(normalizedQuery, System.currentTimeMillis())
    }

    override fun getQueries(limit: Int): Flow<List<QueryLocalDto>> {
        return queryDao.getQueries(limit)
    }

    override suspend fun deleteQueryById(id: Int) {
        queryDao.deleteQueryById(id)
    }

    override suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto) {
        recentViewedDao.insertRecentViewed(recentViewed)
    }

    override fun getAllRecentViewed(
        limit: Int
    ): Flow<List<RecentViewedLocalDto>> {
        return recentViewedDao.getAllRecentViewed(limit)
    }

    override suspend fun deleteAllRecentViewed() {
        recentViewedDao.deleteAllRecentViewed()
    }

    override suspend fun deleteAllQueries() {
        queryDao.deleteAllQueries()
    }

    override suspend fun insertWatchedMediaHistory(item: WatchedMediaHistoryLocalDto) {
        watchedMediaHistoryDao.insertWatchedMediaHistory(item)
    }

    override suspend fun getWatchedMediaHistory(
        username: String,
        mediaType: MediaType?,
        genreId: Int?
    ): Flow<List<WatchedMediaHistoryLocalDto>> {
        return watchedMediaHistoryDao.getWatchedMediaHistory(username, mediaType?.name, genreId?.toString())
    }
}