package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.ContentType
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShow: TvShowLocalDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tvShows: List<TvShowLocalDto>)

    @Query("SELECT * FROM tvshow WHERE (id IN (:ids))")
    suspend fun getTvShowsByIds(ids: List<Int>): List<TvShowLocalDto>

    @Query(
        """
        DELETE FROM tvshow 
        WHERE id NOT IN (
            SELECT item_id FROM cached_content WHERE content_type = (:contentType)
        )
    """
    )
    suspend fun deleteUnreferencedTvShows(contentType: String = ContentType.TV_SHOW.name)
}