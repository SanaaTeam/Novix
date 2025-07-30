package com.sanaa.vod.continueWatch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto

@Dao
interface ContinueWatchingDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdate(item: ContinueWatchingLocalDto)

    @Query("SELECT * FROM continue_watching LIMIT :limit")
    suspend fun getContinueWatchingList(limit: Int): List<ContinueWatchingLocalDto>

    @Query("DELETE FROM continue_watching WHERE mediaId = :mediaId")
    suspend fun deleteItem(mediaId: Int)

    @Query("DELETE FROM continue_watching")
    suspend fun clearAll()
}