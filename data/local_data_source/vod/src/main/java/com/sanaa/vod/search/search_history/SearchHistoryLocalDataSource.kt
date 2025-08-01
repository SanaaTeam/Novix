package com.sanaa.vod.search.search_history

import com.sanaa.vod.continueWatch.dao.WatchedMediaHistoryDao
import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto
import com.sanaa.vod.dataSource.local.search.LocalSearchHistoryDataSource
import com.sanaa.vod.dataSource.local.search.dto.QueryLocalDto
import com.sanaa.vod.dataSource.local.search.dto.RecentViewedLocalDto
import com.sanaa.vod.search.search_history.dao.QueryDao
import com.sanaa.vod.search.search_history.dao.RecentViewedDao
import kotlinx.coroutines.flow.Flow
import usecase.search.search_param.MediaType
import javax.inject.Inject

class LocalSearchHistoryDataSourceImpl @Inject constructor(
    private val queryDao: QueryDao,
    private val recentViewedDao: RecentViewedDao,
    private val watchedMediaHistoryDao: WatchedMediaHistoryDao
) : LocalSearchHistoryDataSource {
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

    override suspend fun addWatchedMedia(item: WatchedMediaHistoryLocalDto) {
        watchedMediaHistoryDao.insert(item)
    }

    override suspend fun getWatchedMedia(
        username: String,
        mediaType: MediaType?,
        genreId: Int?
    ): Flow<List<WatchedMediaHistoryLocalDto>> {
        return watchedMediaHistoryDao.getHistory(username, mediaType?.name, genreId?.toString())
    }
}