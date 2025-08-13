package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.SavedListMovieDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllMovies(movies: List<SavedListMovieDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovie(movie: SavedListMovieDto)

    @Query("DELETE FROM saved_list_movies WHERE list_id = :listId")
    suspend fun deleteAllByListId(listId: Long)

    @Query("DELETE FROM saved_list_movies WHERE movie_id = :movieId")
    suspend fun deleteByMovieId(movieId: Long)

    @Query("SELECT * FROM saved_list_movies WHERE movie_id = :movieId LIMIT 1")
    suspend fun getSavedMovieById(movieId: Long): SavedListMovieDto?

    @Query("SELECT * FROM saved_list_movies")
    suspend fun getSavedMovies(): List<SavedListMovieDto>

    @Query("DELETE FROM saved_list_movies")
    suspend fun deleteAllSavedMovies()

    // ------------- Flow ----------------

    @Query("SELECT * FROM saved_list_movies ORDER BY list_id ASC, movie_id ASC")
    fun observeSavedMovies(): Flow<List<SavedListMovieDto>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_list_movies WHERE movie_id = :movieId AND list_id = :listId LIMIT 1)")
    fun isMovieSavedFlow(movieId: Long, listId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_list_movies WHERE movie_id = :movieId LIMIT 1)")
    fun isMovieSavedFlow(movieId: Long): Flow<Boolean>

}
