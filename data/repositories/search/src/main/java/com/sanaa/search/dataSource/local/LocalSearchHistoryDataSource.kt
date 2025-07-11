package com.sanaa.search.dataSource.local

import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import kotlinx.coroutines.flow.Flow

interface LocalSearchHistoryDataSource {
    suspend fun insertQuery(query: String)
    fun getAllQueries(limit: Int): Flow<List<QueryLocalDto>>
    suspend fun deleteQueryById(id: Int)
    suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto)
    fun getAllRecentViewed(limit: Int): Flow<List<RecentViewedLocalDto>>
    suspend fun deleteAllRecentViewed()
    suspend fun deleteAllQueries()
}