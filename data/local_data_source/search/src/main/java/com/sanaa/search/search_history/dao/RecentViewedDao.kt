package com.sanaa.search.search_history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.search_history.dto.RecentViewedLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentViewed(recentViewed: RecentViewedLocalDto)

    @Query("SELECT * FROM recent_viewed ORDER BY timestamp DESC LIMIT :limit")
    fun getAllRecentViewed(limit: Int): Flow<List<RecentViewedLocalDto>>

    @Query("DELETE FROM recent_viewed")
    suspend fun deleteAllRecentViewed()
}