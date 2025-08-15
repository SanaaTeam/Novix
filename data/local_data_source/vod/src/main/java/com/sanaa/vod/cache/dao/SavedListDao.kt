package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.SavedMovieLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(savedList: SavedListLocalDto)

    @Query("DELETE FROM saved_lists WHERE id = :listId")
    suspend fun deleteList(listId: Int): Int

    @Query("SELECT * FROM saved_lists WHERE id = :listId")
    fun getListById(listId: Int): Flow<SavedListLocalDto?>

    @Query("SELECT * FROM saved_lists")
    fun getAllLists(): Flow<List<SavedListLocalDto>>

    @Query("SELECT * FROM saved_movies WHERE id IN (:movieIds)")
    fun getMoviesByIds(movieIds: List<Int>): Flow<List<SavedMovieLocalDto>>

}