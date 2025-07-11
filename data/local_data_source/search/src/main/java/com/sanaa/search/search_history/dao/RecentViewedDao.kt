package com.sanaa.search.search_history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto)

    @Query("SELECT * FROM recent_viewed ORDER BY timestamp DESC LIMIT :limit")
    fun getAllRecentViewed(limit: Int): Flow<List<RecentViewedLocalDto>>

    @Query("DELETE FROM recent_viewed")
    suspend fun deleteAllRecentViewed()

    @Query("DELETE FROM recent_viewed WHERE timestamp < :timestamp")
    suspend fun deleteOldRecentViewed(timestamp: Long)

    suspend fun insertRecentViewedWithCleanup(recentViewed: RecentViewedLocalDto) {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        deleteOldRecentViewed(oneHourAgo)
        insertRecentViewed(recentViewed)
    }
}