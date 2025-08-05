package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShow: TvShowLocalDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tvShows: List<TvShowLocalDto>)

    @Query("SELECT * FROM tvshow WHERE id = :id LIMIT 1")
    suspend fun getTvShowById(id: Int): TvShowLocalDto?

    @Query("SELECT * FROM tvshow WHERE (id IN (:ids))")
    suspend fun getTvShowsByIds(ids: List<Int>): List<TvShowLocalDto>
}