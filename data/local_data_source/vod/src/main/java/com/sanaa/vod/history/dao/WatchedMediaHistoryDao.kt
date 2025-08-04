package com.sanaa.vod.history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedMediaHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchedMediaHistory(item: WatchedMediaHistoryLocalDto)

    @Query(
        """
        SELECT * FROM watched_media_history
        WHERE username = :username
        AND (:mediaType IS NULL OR media_type = :mediaType)
        AND (:genreIdStr IS NULL OR genres LIKE '%,' || :genreIdStr || ',%')
        ORDER BY timestamp DESC
    """
    )
    fun getWatchedMediaHistory(username: String, mediaType: String?, genreIdStr: String?): Flow<List<WatchedMediaHistoryLocalDto>>
}