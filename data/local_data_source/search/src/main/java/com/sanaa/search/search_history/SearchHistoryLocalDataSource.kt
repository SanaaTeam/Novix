package com.sanaa.search.search_history

import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import kotlinx.coroutines.flow.Flow

class LocalSearchHistoryDataSourceImpl(
    private val queryDao: QueryDao,
    private val recentViewedDao: RecentViewedDao
) : LocalSearchHistoryDataSource {
    override suspend fun insertQuery(query: String) {
        queryDao.insertQuery(QueryLocalDto(query = query))
    }

    override fun getAllQueries(limit: Int): Flow<List<QueryLocalDto>> {
        return queryDao.getAllQueries(limit)
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
}