package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto

@Dao
interface CachedContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contentInfo: CachedContentLocalDto)

    @Query(
        """
        SELECT * FROM cached_content
        WHERE (id = :metadataId)
        AND (:mediaType IS NULL OR media_type = :mediaType)
    """
    )
    suspend fun getCachedContentInfo(
        metadataId: Long,
        mediaType: String? = null,
    ): List<CachedContentLocalDto>
}