package com.sanaa.vod.search.dao

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
        """
        SELECT * FROM movie
        WHERE (:query IS NULL OR LOWER(title) LIKE '%' || LOWER(:query) || '%')
        ORDER BY
            CASE
                WHEN LOWER(title) = LOWER(:query) THEN 1
                WHEN LOWER(title) LIKE LOWER(:query) || '%' THEN 2
                ELSE 3
            END,
            title ASC
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getFilteredMovies(
        query: String, limit: Int, offset: Int,
    ): List<MovieLocalDto>
}