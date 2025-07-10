package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.data.local.entity.SeriesLocalDto

@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesLocalDto)

    @Query("SELECT * FROM series WHERE release_year BETWEEN :minYear AND :maxYear AND genres LIKE :genre AND imdb_rating >= :rating")
    suspend fun getFilteredSeries(minYear: Int, maxYear: Int, genre: String, rating: Float): List<SeriesLocalDto>

} 