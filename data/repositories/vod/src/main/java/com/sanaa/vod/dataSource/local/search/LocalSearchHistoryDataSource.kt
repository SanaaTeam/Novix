package com.sanaa.vod.dataSource.local.search

import com.sanaa.vod.dataSource.local.search.dto.QueryLocalDto
import com.sanaa.vod.dataSource.local.search.dto.RecentViewedLocalDto
import kotlinx.coroutines.flow.Flow

interface LocalSearchHistoryDataSource {
    suspend fun insertQuery(query: String)
    fun getQueries(limit: Int): Flow<List<QueryLocalDto>>
    suspend fun deleteQueryById(id: Int)
    suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto)
    fun getAllRecentViewed(limit: Int): Flow<List<RecentViewedLocalDto>>
    suspend fun deleteAllRecentViewed()
    suspend fun deleteAllQueries()
}