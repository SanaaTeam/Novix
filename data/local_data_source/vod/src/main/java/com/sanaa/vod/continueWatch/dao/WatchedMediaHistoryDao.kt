package com.sanaa.vod.continueWatch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto

@Dao
interface WatchedMediaHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchedMediaHistoryLocalDto)

    @Query(
        """
        SELECT * FROM watched_media_history
        WHERE (:mediaType IS NULL OR media_type = :mediaType)
        AND (:genreIdStr IS NULL OR genres LIKE '%,' || :genreIdStr || ',%')
        ORDER BY timestamp DESC
    """
    )
    suspend fun getHistory(mediaType: String?, genreIdStr: String?): List<WatchedMediaHistoryLocalDto>
}