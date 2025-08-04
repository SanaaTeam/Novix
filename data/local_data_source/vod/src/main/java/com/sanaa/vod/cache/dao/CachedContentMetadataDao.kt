package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto

@Dao
interface CachedContentMetadataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedContentMetadata(metadata: CachedContentMetadataLocalDto): Int

    @Query(
        """
        SELECT * FROM cached_content_metadata
        WHERE (:category IS NULL OR category = :category)
        AND (:language IS NULL OR language = :language) 
        LIMIT 1
    """
    )
    fun getCachedContentMetadata(
        category: String?,
        language: String?
    ): CachedContentMetadataLocalDto

    @Query("DELETE FROM cached_content_metadata WHERE timestamp < :timestamp")
    suspend fun clearExpiredMetadata(timestamp: Long)
}