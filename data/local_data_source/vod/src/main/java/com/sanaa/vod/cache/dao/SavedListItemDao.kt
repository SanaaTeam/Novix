package com.sanaa.vod.cache.dao

import androidx.room.*
import com.sanaa.vod.dataSource.local.cache.dto.SavedListItemLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListItemDao {

    @Query("SELECT movie_id FROM saved_list_item WHERE list_id = :listId ORDER BY added_at DESC")
    fun observeMovieIdsForList(listId: Int): Flow<List<Int>>

    @Query("SELECT movie_id FROM saved_list_item WHERE list_id = :listId ORDER BY added_at DESC")
    suspend fun getMovieIdsForListOnce(listId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(rows: List<SavedListItemLocalDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(row: SavedListItemLocalDto)

    @Query("DELETE FROM saved_list_item WHERE list_id = :listId AND movie_id = :movieId")
    suspend fun delete(listId: Int, movieId: Int)

    @Query("DELETE FROM saved_list_item WHERE list_id = :listId")
    suspend fun deleteByList(listId: Int)

    @Query("DELETE FROM saved_list_item")
    suspend fun clear()

    @Query("SELECT EXISTS(SELECT 1 FROM saved_list_item WHERE movie_id = :movieId)")
    suspend fun isMovieSaved(movieId: Int): Boolean

    @Query("SELECT movie_id FROM saved_list_item GROUP BY movie_id")
    fun observeAllSavedMovieIds(): Flow<List<Int>>
}