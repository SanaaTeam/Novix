package com.sanaa.vod.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieLocalDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieLocalDto>)

    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: Int): MovieLocalDto?

    @Query("SELECT * FROM movie WHERE (id IN (:ids))")
    suspend fun getMoviesByIds(ids: List<Int>): List<MovieLocalDto>

    @Query("""
        DELETE FROM movie 
        WHERE id NOT IN (
            SELECT media_id FROM cached_content WHERE media_type = (:mediaType)
        )
    """)
    suspend fun deleteUnreferencedMovies(mediaType: String = MediaType.MOVIE.name)
}