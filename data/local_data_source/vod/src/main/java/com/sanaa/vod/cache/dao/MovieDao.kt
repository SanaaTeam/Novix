package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.ContentType
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieLocalDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieLocalDto>)

    @Query("SELECT * FROM movie WHERE (id IN (:ids))")
    suspend fun getMoviesByIds(ids: List<Int>): List<MovieLocalDto>

    @Query(
        """
        DELETE FROM movie 
        WHERE id NOT IN (
            SELECT item_id FROM cached_content WHERE content_type = (:contentType)
        )
    """
    )
    suspend fun deleteUnreferencedMovies(contentType: String = ContentType.MOVIE.name)
}