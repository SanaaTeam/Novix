package com.sanaa.vod.cache.dao

import androidx.room.*
import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListDao {

    @Query("SELECT * FROM saved_list ORDER BY title COLLATE NOCASE")
    fun observeAll(): Flow<List<SavedListLocalDto>>

    @Query("SELECT * FROM saved_list ORDER BY title COLLATE NOCASE")
    suspend fun getAllOnce(): List<SavedListLocalDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(lists: List<SavedListLocalDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: SavedListLocalDto)

    @Query("DELETE FROM saved_list WHERE id = :listId")
    suspend fun deleteById(listId: Int)

    @Query("DELETE FROM saved_list")
    suspend fun clear()
}