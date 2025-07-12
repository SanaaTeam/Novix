package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: TvSeriesLocalDto)

    @Query("SELECT * FROM tv_series WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'")
    suspend fun getFilteredSeries(query: String): List<TvSeriesLocalDto>

} 