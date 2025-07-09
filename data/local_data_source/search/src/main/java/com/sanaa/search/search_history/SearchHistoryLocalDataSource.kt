package com.sanaa.search.search_history

import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import com.sanaa.search.search_history.dto.QueryLocalDto
import com.sanaa.search.search_history.dto.RecentViewedLocalDto
import kotlinx.coroutines.flow.Flow

class SearchHistoryLocalDataSource(
    private val queryDao: QueryDao,
    private val recentViewedDao: RecentViewedDao
) {
    suspend fun insertQuery(query: String) {
        queryDao.insertQuery(QueryLocalDto(query = query))
    }

    suspend fun deleteQuery(query: String) {
        queryDao.deleteQuery(query)
    }

    fun getAllQueries(): Flow<List<String>> {
        return queryDao.getAllQueries()
    }

    suspend fun deleteQueryById(id: Int) {
        queryDao.deleteQueryById(id)
    }

    suspend fun insertRecentViewed(title: String, imageUrl: String, isSaved: Boolean) {
        recentViewedDao.insertRecentViewed(
            RecentViewedLocalDto(
                title = title,
                imageUrl = imageUrl,
                isSaved = isSaved
            )
        )
    }

    fun getAllRecentViewed(): Flow<List<RecentViewedLocalDto>> {
        return recentViewedDao.getAllRecentViewed()
    }

    suspend fun deleteAllRecentViewed() {
        recentViewedDao.deleteAllRecentViewed()
    }

}