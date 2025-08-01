package com.sanaa.vod.history.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedMediaHistoryDao {

    @Upsert
    suspend fun upsert(item: WatchedMediaHistoryLocalDto)

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