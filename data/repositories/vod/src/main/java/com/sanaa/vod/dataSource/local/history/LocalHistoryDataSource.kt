package com.sanaa.vod.dataSource.local.history

import com.sanaa.vod.dataSource.local.history.dto.search.QueryLocalDto
import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import kotlinx.coroutines.flow.Flow

interface LocalHistoryDataSource {
    suspend fun insertQuery(query: String)
    fun getQueries(limit: Int): Flow<List<QueryLocalDto>>
    suspend fun deleteQueryById(id: Int)
    suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto)
    fun getAllRecentViewed(limit: Int): Flow<List<RecentViewedLocalDto>>
    suspend fun deleteAllRecentViewed()
    suspend fun deleteAllQueries()
    suspend fun insertWatchedMediaHistory(item: WatchedMediaHistoryLocalDto)
    suspend fun getWatchedMediaHistory(username: String, mediaType: String?, genreId: Int?): Flow<List<WatchedMediaHistoryLocalDto>>
}