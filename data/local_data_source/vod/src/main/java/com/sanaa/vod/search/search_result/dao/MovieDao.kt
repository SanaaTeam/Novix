package com.sanaa.vod.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieLocalDto)

    @Query("SELECT * FROM movie WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): MovieLocalDto?

    @Query(
        " SELECT * FROM movie WHERE id IN (:moviesIds) LIMIT :limit OFFSET :offset"
    )
    suspend fun getPagedMoviesByIds(
        moviesIds: List<Int>,
        limit: Int,
        offset: Int
    ): List<MovieLocalDto>
}