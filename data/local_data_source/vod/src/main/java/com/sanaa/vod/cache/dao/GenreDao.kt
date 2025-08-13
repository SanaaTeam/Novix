package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.ContentType
import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: GenreLocalDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreLocalDto>)

    @Query("SELECT * FROM genre WHERE (id IN (:ids))")
    suspend fun getGenreByIds(ids: List<Int>): List<GenreLocalDto>

    @Query(
        """
        DELETE FROM genre 
        WHERE id NOT IN (
            SELECT item_id FROM cached_content WHERE content_type = (:contentType)
        )
    """
    )
    suspend fun deleteUnreferencedGenres(contentType: String = ContentType.GENRE.name)
}