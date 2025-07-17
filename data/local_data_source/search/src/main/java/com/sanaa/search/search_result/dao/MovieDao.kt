package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.MovieLocalDto

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieLocalDto)

    @Query(
        """
        SELECT * FROM movie 
        WHERE (:query IS NULL OR LOWER(title) LIKE '%' || LOWER(:query) || '%')
    """
    )
    suspend fun getFilteredMovies(
        query: String
    ): List<MovieLocalDto>
}