package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: TvSeriesLocalDto)

    @Query("SELECT * FROM tv_series WHERE release_year BETWEEN :minYear AND :maxYear AND genres LIKE :genre AND imdb_rating >= :rating")
    suspend fun getFilteredSeries(minYear: Int, maxYear: Int, genre: String, rating: Float): List<TvSeriesLocalDto>

} 