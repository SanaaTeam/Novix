package com.sanaa.vod.continueWatch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto

@Dao
interface ContinueWatchingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ContinueWatchingLocalDto)

    @Query("SELECT * FROM continue_watching WHERE username = :username LIMIT :limit")
    suspend fun getContinueWatchingList(
        username: String,
        limit: Int
    ): List<ContinueWatchingLocalDto>

    @Query("DELETE FROM continue_watching WHERE mediaId = :mediaId AND username = :username")
    suspend fun deleteItem(mediaId: Int, username: String)

    @Query("DELETE FROM continue_watching")
    suspend fun clearAll()
}