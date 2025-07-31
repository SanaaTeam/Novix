package com.sanaa.vod.continueWatch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ContinueWatchingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ContinueWatchingLocalDto)

    @Query("SELECT * FROM continue_watching WHERE username = :username LIMIT :limit")
    fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingLocalDto>>
}